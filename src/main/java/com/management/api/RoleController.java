//package com.management.api;
//
//
//
//import com.management.exceptions.BadRequestException;
//import com.management.exceptions.ConflictException;
//import com.management.model.Settings.*;
//import com.management.model.Users.User;
//import com.management.service.*;
//import com.management.utility.AuditTrailFlag;
//import com.management.utility.CustomResponseCode;
//import com.management.utility.TableNames;
//import com.management.utility.Utility;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//
//
//@RestController
//@RequestMapping(value = "/api")
//public class RoleController {
//
//
//    private final static Logger logger = LoggerFactory.getLogger(RoleController.class);
//
//    final String[] ALLOWED_FIELDS = new String[]{"name", "id"};
//    @Autowired
//    private RoleService roleService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    UtilityService utilityService;
//    @Autowired
//    private AuditTrailService auditTrailService;
//
//
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        binder.setAllowedFields(ALLOWED_FIELDS);
//    }
//
//    @RequestMapping(value = "/roles/{roleId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.OK)
//    public Role role(@PathVariable Long roleId) {
//        if (roleId == null || roleId.toString().isEmpty())
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role ID cannot be empty");
//        Role role = roleService.find(roleId);
//        if (role == null) {
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role does not exist");
//        }
//        return role;
//    }
//
//
//    @RequestMapping(value = "/roles", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.CREATED)
//    // @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CREATE_ROLE')")
//    public ResponseEntity<Response> create(@RequestBody @Validated Role role) {
//        HttpStatus httpCode;
//        Response resp = new Response();
//        if (role.getName() == null || role.getName().isEmpty())
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role Name cannot be empty");
//        if (!CustomResponseCode.USER_CATEGORY_MERCHANT.equals(role.getRoleType())
//                && !CustomResponseCode.USER_CATEGORY_SUPER_USER.equals(role.getRoleType()))
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Role Type");
//        if (roleService.getRoleByName(role) != null) {
//            throw new ConflictException(CustomResponseCode.INVALID_REQUEST, " Role name already exist");
//        }
//        User user = TokenService.getCurrentUserFromSecurityContext();
//        role.setCreatedBy(user.getId());
//        roleService.create(role);
//        resp.setCode(CustomResponseCode.SUCCESS);
//        resp.setDescription("Successful");
//        httpCode = HttpStatus.CREATED;
//
//        return new ResponseEntity<>(resp, httpCode);
//    }
//
//
//    @RequestMapping(value = "/role/disableenable", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACTIVATE_USER')")
//    public ResponseEntity<?> disableEnable(@RequestBody @Validated DisableEnable data) throws Exception {
//
//        User user = TokenService.getCurrentUserFromSecurityContext();
//
//        if (data.getStatus() == CustomResponseCode.ENABLE || data.getStatus() == CustomResponseCode.DISABLE) {
//            data.setChecker(user.getId());
//            data.setTableName(TableNames.role.name());
//
//            Response resp = new Response();
//
//            HttpStatus httpCode;
//
//            Long response = utilityService.disableEnable(data);
//
//            if (response == CustomResponseCode.DB_SUCCESS) {
//                resp.setCode(CustomResponseCode.SUCCESS);
//                resp.setDescription("successful");
//                httpCode = HttpStatus.OK;
//            } else {
//                resp.setCode(CustomResponseCode.FAILED);
//                resp.setDescription("failed");
//                httpCode = HttpStatus.INTERNAL_SERVER_ERROR;
//
//            }
//
//            return new ResponseEntity<>(resp, httpCode);
//
//        } else
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid status");
//    }
//
//
//    @RequestMapping(value = "/role/approvereject", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ACTIVATE_USER')")
//    public ResponseEntity<?> approveReject(@RequestBody @Validated ApproveReject data) throws Exception {
//
//        User user = TokenService.getCurrentUserFromSecurityContext();
//
//        if (data.getApproved() == CustomResponseCode.ENABLE || data.getApproved() == CustomResponseCode.DISABLE) {
//            data.setChecker(user.getId());
//            data.setTableName(TableNames.role.name());
//
//            Response resp = new Response();
//
//            HttpStatus httpCode;
//
//            Long response = utilityService.approveReject(data);
//
//            if (response == CustomResponseCode.DB_SUCCESS) {
//                resp.setCode(CustomResponseCode.SUCCESS);
//                resp.setDescription("successful");
//                httpCode = HttpStatus.OK;
//            } else {
//                resp.setCode(CustomResponseCode.FAILED);
//                resp.setDescription("failed");
//                httpCode = HttpStatus.INTERNAL_SERVER_ERROR;
//            }
//            return new ResponseEntity<>(resp, httpCode);
//        } else
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid status");
//
//    }
//
//
//    @RequestMapping(value = "/roles", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
//    //@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_UPDATE_ROLE')")
//    public ResponseEntity<Response> update(@RequestBody @Validated Role role, HttpServletRequest request) {
//        HttpStatus httpCode;
//        Response resp = new Response();
//        if (role.getId() == null || role.toString().isEmpty())
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role ID cannot be empty");
//        if (role.getName() == null || role.getName().isEmpty())
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role Name cannot be empty");
//        if (!CustomResponseCode.USER_CATEGORY_MERCHANT.equals(role.getRoleType())
//                && !CustomResponseCode.USER_CATEGORY_SUPER_USER.equals(role.getRoleType()))
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Role Type");
//
//        Role check = roleService.getRoleByName(role);
//        if (check != null) {
//            throw new ConflictException(CustomResponseCode.CONFLICT_EXCEPTION, " Role name already exist");
//        }
//        //TODO need check
//
//        if (check != null) {
//            if (check.getRoleType() != role.getRoleType()) {
//                throw new BadRequestException(CustomResponseCode.INVALID_REQUEST,
//                        "Invalid request, you cannot change existing role type");
//            }
//        }
//        role.setId(role.getId());
//        User user = TokenService.getCurrentUserFromSecurityContext();
//        role.setLastUpdatedBy(user.getId());
//        roleService.update(role);
//        resp.setCode(CustomResponseCode.SUCCESS);
//        resp.setDescription("Update Successful");
//        httpCode = HttpStatus.OK;
//        auditTrailService
//                .logEvent(user.getUsername(),
//                        " Role Updated by username:" + user.getUsername(),
//                        AuditTrailFlag.UPDATE,
//                        "Role Updated request for:" + role.toString()
//                        , 1, Utility.getClientIp(request));
//
//        return new ResponseEntity<>(resp, httpCode);
//
//    }
//
//
//    @RequestMapping(value = "/roles/{roleId}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.OK)
//    // @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELETE_ROLE')")
//    public ResponseEntity<Response> delete(@PathVariable Long roleId) throws Exception {
//        HttpStatus httpCode;
//        Response resp = new Response();
//        if (roleId == null || roleId.toString().isEmpty())
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role ID cannot be empty");
//        if (roleService.find(roleId) == null) {
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role does not exist");
//        }
//        roleService.delete(roleId);
//        resp.setCode(CustomResponseCode.SUCCESS);
//        resp.setDescription("Role removed successfully");
//        httpCode = HttpStatus.OK;
//        return new ResponseEntity<>(resp, httpCode);
//    }
//
//
//    @RequestMapping(value = "/role/{userid}", method = RequestMethod.GET,
//            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.OK)
//    // @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_VIEW_USER_ROLE')")
//    public List<Role> getUserRole(@PathVariable Long userid) {
//        if (userid == null || userid.toString().isEmpty())
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid User ID");
//        return roleService.getUserRole(userid);
//    }
//
//
//    @RequestMapping(value = "/roles/{userId}/{roleId}", method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.CREATED)
//    //  @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ADD_USER_ROLE')")
//    public ResponseEntity<Response> addUserRoles(@PathVariable Long userId, @PathVariable Long roleId) {
//        HttpStatus httpCode;
//        Response resp = new Response();
//        if (userId == null)
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "User ID cannot be empty");
//        if (roleId == null)
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role ID cannot be empty");
//        if (roleService.find(roleId) == null) {
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role does not exist");
//        }
//        if (userService.find(userId) == null) {
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Username.");
//        }
//        if (roleService.isUserRole(userId, roleId) != null) {
//            throw new ConflictException(CustomResponseCode.CONFLICT_EXCEPTION, " Role already assigned to User");
//        }
//        User user = TokenService.getCurrentUserFromSecurityContext();
//        roleService.addUserRoles(userId, roleId, user.getId());
//        resp.setCode(CustomResponseCode.SUCCESS);
//        resp.setDescription("Successful");
//        httpCode = HttpStatus.OK;
//        return new ResponseEntity<>(resp, httpCode);
//    }
//
//
//    @RequestMapping(value = "/roles/{userId}/{roleId}", method = RequestMethod.PUT,
//            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.NO_CONTENT)
//    //  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
//    public ResponseEntity<Response> updateUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
//        HttpStatus httpCode;
//        Response resp = new Response();
//        if (userId == null)
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "User ID cannot be empty");
//        if (roleId == null)
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role ID cannot be empty");
//        if (roleService.find(roleId) == null) {
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role does not exist");
//        }
//        if (userService.find(userId) == null) {
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Username.");
//        }
//        User user = TokenService.getCurrentUserFromSecurityContext();
//        roleService.updateUserRole(userId, roleId, user.getRoleId());
//        resp.setCode(CustomResponseCode.SUCCESS);
//        resp.setDescription("Successful");
//        httpCode = HttpStatus.OK;
//
//        return new ResponseEntity<>(resp, httpCode);
//    }
//
//
//    @RequestMapping(value = "/roles/{userId}/{roleId}", method = RequestMethod.DELETE,
//            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(value = HttpStatus.OK)
//    //@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DELETE_USER_ROLE')")
//    public ResponseEntity deletUserRole(@PathVariable Long userId, @PathVariable Long roleId, HttpServletRequest request) {
//        HttpStatus httpCode;
//        Response resp = new Response();
//        if (userId == null)
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "User ID cannot be empty");
//        if (roleId == null)
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role ID cannot be empty");
//        Role role = roleService.find(roleId);
//        if (role == null) {
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Role does not exist");
//        }
//        if (userService.find(userId) == null) {
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Username.");
//        }
//        User user = TokenService.getCurrentUserFromSecurityContext();
//        roleService.deleteUserRole(userId, roleId, user.getId());
//
//        resp.setCode(CustomResponseCode.SUCCESS);
//        resp.setDescription("Successful");
//        httpCode = HttpStatus.OK;
//
//        auditTrailService
//                .logEvent(user.getUsername(),
//                        "Delete User Role by username:" + user.getUsername(),
//                        AuditTrailFlag.UPDATE,
//                        "Delete User Role request :" + role.toString() + " for User :" + userId
//                        , 1, Utility.getClientIp(request));
//        return new ResponseEntity<>(resp, httpCode);
//    }
//
//
//
//
//}