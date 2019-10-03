package com.sygn.xeapp.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.sygn.xeapp.model.LoginUserModel;
import com.sygn.xeapp.model.UserModel;
import com.sygn.xeapp.registration.SignInScreen;
import com.sygn.xeapp.registration.WelcomeScreen;

import java.util.HashMap;

public class SharedPrefManager {

    // Shared pref mode
    int PRIVATE_MODE = 0;


    //the constants
    private static final String SHARED_PREF_NAME = "xeridesharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_UNIQE_TYPE = "keyuuniqueid";
    private static final String KEY_EMAIL = "keyemail";
    public static final String KEY_PHONE_NUM = "keynumber";
    public static final String KEY_SMS_CODE = "keycode";
    public static final String KEY_ID = "keyid";
    public static final String KEY_PASSWORD= "password";
    public static final String KEY_PHONE = "phone";
    public static final String ACCESS_TOKEN = "accesstoken";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
//    public void userLogin(UserModel user) {
//         sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//         editor = sharedPreferences.edit();
//        editor.putString(KEY_ID, user.getUnique_id());
//        editor.putString(KEY_EMAIL, user.getEmail());
//        editor.putString(KEY_PHONE_NUM, user.getMobile());
//        editor.putString(KEY_UNIQE_TYPE, user.getUserType());
//        editor.apply();
//    }

    public void userLogin(LoginUserModel user) {
        sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getUserId());
        editor.putString(KEY_PASSWORD, user.getUserPassword());
        editor.putString(KEY_PHONE, user.getUserPhone());
        editor.putString(ACCESS_TOKEN, user.getAccessToken());
        editor.apply();
    }

    public void userPhoneCode(String smsCode, String phoneNum){

        editor.putString(KEY_SMS_CODE, smsCode);
        editor.putString(KEY_PHONE_NUM, phoneNum);

        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getPhoneCode(){
        HashMap<String, String> code = new HashMap<String, String>();
        // user name
        code.put(KEY_SMS_CODE, sharedPreferences.getString(KEY_SMS_CODE, null));
        code.put(KEY_PHONE_NUM, sharedPreferences.getString(KEY_PHONE_NUM, null));

        // user email id
//        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
//        //user id
//        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));

        // return user
        return code;

    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_ID, null) != null;
    }

    //this method will give the logged in user
    public LoginUserModel getUser() {
         sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new LoginUserModel(
                sharedPreferences.getString(KEY_ID, ""),
                sharedPreferences.getString(KEY_PASSWORD, null),
                sharedPreferences.getString(KEY_PHONE, null),
                sharedPreferences.getString(ACCESS_TOKEN, null)
        );
    }

    //this method will logout the user
    public void logout() {
         sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
         editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        //mCtx.startActivity(new Intent(mCtx, WelcomeScreen.class));

        // After logout redirect user to Loing ActivityFragment
        Intent i = new Intent(mCtx, WelcomeScreen.class);

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new ActivityFragment
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login ActivityFragment
        mCtx.startActivity(i);
    }

}
