package com.management.model.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.management.utility.CustomDateSerializer;


import java.io.Serializable;
import java.sql.Timestamp;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccessTokenWithUserDetails implements Serializable{


    @JsonProperty("accessToken")
    private String accessToken;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("menu")
    private String menu;

//    @JsonIgnore
    @JsonProperty("category")
    private String category;


    //@JsonProperty("encryptedId")
    //private String encryptedId;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("firstName")
    private String firstName;




//    @JsonIgnore
//    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonProperty("lastLogin")
    private Timestamp lastLogin;


    @JsonIgnore
    @JsonProperty("passwordExpirationDaysRemaining")
    private int passwordExpirationDaysRemaining;

    @JsonIgnore
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonProperty("failedLoginDate")
    private Timestamp failedLoginDate;

    @JsonIgnore
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonProperty("passwordChangedOn")
    private Timestamp passwordChangedOn;

    @JsonProperty("TokenExpiry")
    private long tokenExpiry;


    @JsonProperty("id")
    private long id;



    public AccessTokenWithUserDetails(String token, User user, String menu, long tokenExpiry) {
        this.accessToken = token;

        this.email = user.getEmail();
        this.category = user.getCategory();
        this.userName = user.getUsername();
        this.phoneNumber = user.getPhoneNumber();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.failedLoginDate = user.getFailedLoginDate();
        this.lastLogin = user.getLastLogin();
        this.passwordChangedOn = user.getPasswordChangedOn();
        this.passwordExpirationDaysRemaining = user.getPasswordExpirationDaysRemaining();
        this.menu = menu;
        this.tokenExpiry = tokenExpiry;
        this.id=user.getId();




    }

}
