package com.sygn.xeapp.utility;


import org.json.JSONArray;

public class Constants {

    public static final int socketTimeout = 30000;

    public static final String BASE_URL = "http://ec2-18-218-128-242.us-east-2.compute.amazonaws.com/";
    public static final String USER_REG_NUMBER = "https://api.xeride.com/api/VerifyPhoneNumber";
    public static final String USER_REGISTRATION_URL = "https://api.xeride.com/api/Account/RegisterRider";
    public static final String GET_XERIDE_LIST = "https://api.xeride.com/api/XErideService/GetXErideServices";
    public static final String GET_XE_SERVICE= "https://api.xeride.com/api/LocationManager/FindXERideService";
    public static final String Confirm_Ride_StepOne= "https://api.xeride.com/api/RideRequest/ConfirmRideStepOne";
    public static final String FIND_XESERVICES= "https://api.xeride.com/api/LocationManager/FindXERideService";
    public static final String UPDATE_XERIDE_STATUS= "https://api.xeride.com/api/RideRequest/UpdateRideStatus";

    public static final String KEY_CURRENT_XE = "currentProduct";
    public static final String RIDE_STATUS = "rideStatus";

    public static final String REGISTER_OPERATION = "registerPhoneNum";
    public static final String LOGIN_OPERATION = "login";
    public static final String CHANGE_PASSWORD_OPERATION = "chgPass";

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    public static final String IS_LOGGED_IN = "isLoggedIn";

    private static Constants constantValues;
    private static String key ="";

    public static Constants getContstantInstance(){
        if (constantValues !=null){

            return constantValues;

        }else {
            constantValues = new Constants();
            return constantValues;
        }
    }

    public JSONArray jsonArray;

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
