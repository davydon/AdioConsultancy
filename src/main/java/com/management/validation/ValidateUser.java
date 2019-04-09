package com.management.validation;


import com.management.exceptions.BadRequestException;
import com.management.model.Users.LoginRequest;
import com.management.model.Users.User;
import com.management.service.UserService;
import com.management.service.UtilityService;
import com.management.utility.CustomResponseCode;
import com.management.utility.PasswordUtil;
import com.management.utility.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ValidateUser {
    private static final Logger logger = LoggerFactory.getLogger(ValidateUser.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UtilityService utilityService;



    public void validatePassword(String username, String password, String oldPassword) {
        if (username == null || username.isEmpty())
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Username cannot be empty");

        if (password == null || password.isEmpty())
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Password cannot be empty");


        if (oldPassword == null || oldPassword.isEmpty())
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Old Password cannot be empty");

        if (!PasswordUtil.passwordValidator(password))
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Password Format");
    }

    public void validateLogin(LoginRequest loginRequest) {

        if (loginRequest.getUsername() == null || loginRequest.getUsername().isEmpty()) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Username");
        }


        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Password ");
        }
    }

    public void validationCreateUser(User user) {

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Username");
        }

//        if (!PasswordUtil.userNameValidator(user.getUsername().trim())) {
//            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Username Format");
//        }


        if (user.getEmail() == null || user.getEmail().isEmpty()) {

            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Email ");
        }
        if (!Utility.validEmail(user.getEmail().trim()))
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Email Address");


        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid FirstName ");
        }

        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Last Names ");
        }

        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Phone Number ");
        }
        if (!Utility.isNumeric(user.getPhoneNumber()))
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Mobile Number format");

        if (user.getPhoneNumber().length() < 8 || user.getPhoneNumber().length() > 14)//PHONE NUMBER LENGTH********** TODO
            throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Mobile Number Length");




            if (user.getAddress() == null || user.getAddress().isEmpty()) {
                throw new BadRequestException(CustomResponseCode.INVALID_REQUEST, "Invalid Address");
            }

//            logger.info(String.format("::::::: Corp Setting for %s",user.getHospitalId()));
//            String dpt = corporateUtil.verifyCorporateSetting(user.getHospitalId(), "Department");
//            logger.info(String.format("::::::: Corp Setting returned %s",dpt));







        }


    }
