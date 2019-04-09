package com.management.utility;


public class CustomResponseCode {

    public static final String DEFAULT = "0";
    public static final String FAILED = "101";

    public static final String INVALID_REQUEST = "01";
    public static final String SUCCESS_200 = "200";
   
    public static final String SUCCESS_00 = "00";
    public static final String SUCCESS_90000 = "90000";


    public static final String CONFLICT_EXCEPTION = "07";
    public static final String LOCKED_EXCEPTION = "423";

    public static final String NOT_FOUND_EXCEPTION = "04";
    public static final String SUCCESS = "00";


    public static final String CHANGE_P_REQUIRED = "15";

    public static final String UNAUTHORIZED = "02";
    public static final String OTHERS = "05";



    public static final int DB_SUCCESS = 1;



    public static final String USER_CATEGORY_USERS = "U";
    public static final String USER_CATEGORY_SUPER_USER = "S";

    public static final String FORGET_PASSWORD = "F";
    public static final String LOGIN = "L";
    public static final String CREATE_USER = "C";


    public static final int DEACTIVE_USER = 2;
    public static final int INACTIVE_USER = 0;
    public static final int DISABLE = 0;
    public static final int ENABLE = 1;
}
