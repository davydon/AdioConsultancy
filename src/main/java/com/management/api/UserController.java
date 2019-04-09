package com.management.api;



import com.management.exceptions.BadRequestException;
import com.management.exceptions.ConflictException;
import com.management.exceptions.LockedException;
import com.management.exceptions.UnauthorizedException;
import com.management.model.Settings.*;
import com.management.model.Users.AccessTokenWithUserDetails;
import com.management.model.Users.LoginRequest;
import com.management.model.Users.User;
import com.management.security.AuthenticationWithToken;
import com.management.service.*;
import com.management.utility.*;
import com.management.validation.ValidateUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping(value = "/api")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ValidateUser validateUser;
    @Autowired
    private RoleService roleService;
    @Autowired
    private LoadData loadData;

    @Autowired
    UtilityService utilityService;
    @Autowired


    @Value("${xpress.login.attempts}")
    private String loginAttempts;




    @RequestMapping(value = "/user/authenticate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {
        validateUser.validateLogin(loginRequest);
        String loginStatus;
        String ipAddress = Utility.getClientIp(request);
        User user = userService.loginUser(loginRequest);
        if (user != null) {
            if (user.isLoginStatus()) {
                //FIRST TIME LOGIN
                if (user.getPasswordChangedOn() == null || CustomResponseCode.INACTIVE_USER == user.getStatus()) {
                    Response resp = new Response();
                    resp.setCode(CustomResponseCode.CHANGE_P_REQUIRED);
                    resp.setDescription("Change password Required, account has not been activated");
                    return new ResponseEntity<>(resp, HttpStatus.ACCEPTED);//202
                }
                if (user.getPasswordExpired()) {
                    Response resp = new Response();
                    resp.setCode(CustomResponseCode.CHANGE_P_REQUIRED);
                    resp.setDescription("Change password Required, Password has expired");
                    return new ResponseEntity<>(resp, HttpStatus.ACCEPTED);//202
                }
                if (CustomResponseCode.DEACTIVE_USER == user.getStatus()) {
                    Response resp = new Response();
                    resp.setCode(CustomResponseCode.FAILED);
                    resp.setDescription("User Account Deactivated, please contact Administrator");
                    return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                if (user.getLoginAttempts() >= Integer.parseInt(loginAttempts) || user.getLockedDate() != null) {
                    // lock account after x failed attempts or locked date is not null
                    userService.lockLogin(user.getId());
                    throw new LockedException(CustomResponseCode.LOCKED_EXCEPTION, "Your account has been locked, kindly contact System Administrator");
                }
            } else {
                //update login failed count and failed login date
                loginStatus = "failed";

                userService.updateFailedLogin(loginRequest.getUsername());
                throw new UnauthorizedException(CustomResponseCode.UNAUTHORIZED, "Invalid Login details.");
            }
        } else if (user == null) {
            //NO NEED TO update login failed count and failed login date SINCE IT DOES NOT EXIST
            throw new UnauthorizedException(CustomResponseCode.UNAUTHORIZED, "Login details does not exist");
        }
        String accessList = roleService.getPermissionsByUserId(user.getId());
        AuthenticationWithToken authWithToken = new AuthenticationWithToken(user, null, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER," + accessList));
        String newToken = this.tokenService.generateNewToken();
        authWithToken.setToken(newToken);
        tokenService.store(newToken, authWithToken);
        SecurityContextHolder.getContext().setAuthentication(authWithToken);

        userService.updateLogin(loginRequest.getUsername(), true);

        AccessTokenWithUserDetails details = new AccessTokenWithUserDetails(newToken, user,
                accessList, userService.getSessionExpiry());

        return new ResponseEntity<>(details, HttpStatus.OK);

    }


    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CREATE_USER')")
    public ResponseEntity<?> createUser(@RequestBody @Validated User user, HttpServletRequest request) {

        validateUser.validationCreateUser(user);
        HttpStatus httpCode = HttpStatus.INTERNAL_SERVER_ERROR;
        Response resp = new Response();
        User usernEW = new User();

        User userExist = userService.isUserExists(user.getUsername(), user.getEmail());
        if (userExist != null) {
            throw new ConflictException(CustomResponseCode.CONFLICT_EXCEPTION, " User account already exist");
        } else {
            String activationCode = Utility.guidID();
            String uri = Utility.getServerURl(request, false, "");
            String password = Utility.getSaltString();
            user.setPassword(passwordEncoder.encode(password));
            //  user.setCreatedBy(userCurrent.getId());
            user.setActivationCode(activationCode);
            User response = userService.create(user);
            if (response != null) {
                if (response.getId() > 0) {
                    if (user.getRoleId() != null) {
                        if (roleService.find(user.getRoleId()) == null) {
                            userService.delete(response.getId());
                            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role does not exist");
                        }
                        if (userService.find(response.getId()) == null) {
                            userService.delete(response.getId());
                            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Username.");
                        }
                        // User currentUser = TokenService.getCurrentUserFromSecurityContext();

                        roleService.addUserRoles(response.getId(), user.getRoleId(), user.getId());
                    }
                    resp.setCode(CustomResponseCode.SUCCESS);
                    resp.setDescription("Successful");
                    httpCode = HttpStatus.CREATED;
                }
            } else {
                resp.setCode(CustomResponseCode.FAILED);
                resp.setDescription("Not successful");
                httpCode = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return new ResponseEntity<>(resp, httpCode);
    }


    @RequestMapping(value = "/user/updatepassword", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePassword(@RequestHeader("USERNAME") String username, @RequestHeader("PASSWORD") String password,
                                            @RequestHeader("ACTIVATECODE") String activateCode) {
        if (username == null || username.isEmpty())
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Username cannot be empty");
        if (password == null || password.isEmpty())
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Password cannot be empty");
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Username.");
        }
        if (userService.passwordUsedRecently(user.getId(), password)) {
            throw new ConflictException(CustomResponseCode.CONFLICT_EXCEPTION, " Password already used");
        }
        if (userService.activateAccount(activateCode, username).equals("0")) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Activation Code invalid/expired");
        }
        Long activatePassword = userService.changePassword(user.getId(), password);
        Response resp = new Response();
        HttpStatus httpCode;
        if (activatePassword > 0L) {
            resp.setCode(CustomResponseCode.SUCCESS);
            resp.setDescription("Password activation successful");
            httpCode = HttpStatus.OK;
        } else {
            resp.setCode(CustomResponseCode.FAILED);
            resp.setDescription("Password activation failed");
            httpCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resp, httpCode);
    }


    @RequestMapping(value = "/user/forgetpassword", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Response> forgotPassword(@RequestBody @Validated User user, HttpServletRequest request) {
        HttpStatus httpCode;
        Response resp = new Response();
        if (user.getEmail() == null || user.getEmail().isEmpty())
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Email address cannot be empty");
        User u = userService.getUserByParameter(user.getEmail());
        if (u == null) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid email address");
        }
        String uri = Utility.getServerURl(request, false, "");
        String activationCode = Utility.guidID();
        //update user account
        userService.updateAccountCode(u.getId(), activationCode);
        String password = Utility.getSaltString();
        resp.setCode(CustomResponseCode.SUCCESS);
        resp.setDescription("Successful");
        httpCode = HttpStatus.OK;


        return new ResponseEntity<>(resp, httpCode);
    }


    @RequestMapping(value = "/user/changepassword", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestHeader("OLDPASSWORD") String oldPassword,
                                            @RequestHeader("PASSWORD") String password,
                                            @RequestHeader("USERNAME") String username) {
        validateUser.validatePassword(username, password, oldPassword);
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Username");
        }
        if (userService.passwordUsedRecently(user.getId(), password)) {
            throw new ConflictException(CustomResponseCode.INVALID_REQUEST, "Password already used");
        }
        if (!userService.passwordUsedRecently(user.getId(), oldPassword)) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Old password");
        }
        Long userId = userService.changePassword(user.getId(), password);


        Response resp = new Response();
        HttpStatus httpCode;
        if (userId > 0L) {
            resp.setCode(CustomResponseCode.SUCCESS);
            resp.setDescription("Change password successful");
            httpCode = HttpStatus.OK;
        } else {
            resp.setCode(CustomResponseCode.FAILED);
            resp.setDescription("Change password failed");
            httpCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resp, httpCode);
    }


    @RequestMapping(value = "/user/unlock", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    // @PreAuthorize("hasAnyRole('ROLE_CORPORATE_ADMIN','ROLE_ADMIN')")
    public void unlockAccount(@RequestBody @Validated User userLock) {
        if (userLock.getUsername() == null || userLock.getUsername().isEmpty())
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Username cannot be empty");
        User user = userService.findByUsername(userLock.getUsername());
        if (user == null) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Username.");
        }
        userService.unlockLogin(user.getId());
    }





    @RequestMapping(value = "/user/logout", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public boolean logout() {
        try {
            AuthenticationWithToken auth = (AuthenticationWithToken) SecurityContextHolder.getContext().getAuthentication();
            return tokenService.remove(auth.getToken());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            LoggerUtil.logError(logger, ex);
        }
        return false;
    }


}