package com.sygn.xeapp.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sygn.xeapp.R;
import com.sygn.xeapp.model.UserModel;
import com.sygn.xeapp.network.CustomVolleyRequest;
import com.sygn.xeapp.preferences.SharedPrefManager;
import com.sygn.xeapp.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

public class SignUpScreen extends AppCompatActivity implements View.OnClickListener {
    private EditText reg_name, reg_email, reg_mobile, reg_password;
    private static final String TAG = "";
    private Button signUp;
    private ProgressBar progressBar;
    private UserModel userModel;
    private String phoneNumber;
    SharedPrefManager sharedPrefManager;
    private ProgressDialog progressDialog;
    private String UUID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);
        reg_mobile = findViewById(R.id.reg_editPhoneNumber);
        reg_email = (EditText) findViewById(R.id.reg_editEmail);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        reg_password = (EditText) findViewById(R.id.reg_editPassword);
        signUp = (Button) findViewById(R.id.sign_up);
        // progressBar = findViewById(R.id.progress);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        HashMap<String, String> user = sharedPrefManager.getPhoneCode();
        phoneNumber = user.get(SharedPrefManager.KEY_PHONE_NUM);
        UUID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserId", "");
        Log.d("preUserId", UUID);

        signUp.setOnClickListener(this);
        reg_mobile.setText(phoneNumber);
        reg_mobile.setFocusable(false);
        reg_mobile.setEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Number verifying...");

    }

    @Override
    public void onClick(View view) {
        registerUser();

    }

    private void registerUser() {
        progressBar.setVisibility(View.VISIBLE);
        final String email = reg_email.getText().toString().trim();
        final String password = reg_password.getText().toString().trim();

        //first we will do the validations



        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            reg_email.setError("Enter a valid email");
            reg_email.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            reg_password.setError("Enter a password");
            reg_password.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }else if(password.length()<6){
            reg_password.setError("Enter a password above 6 character");
            progressBar.setVisibility(View.GONE);
            return;
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.USER_REGISTRATION_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("heelo", "onResponse: "+response);


                        try {
                            //converting response to json object
                            JSONObject json= (JSONObject) new JSONTokener(response).nextValue();
                            JSONObject userResult = json.getJSONObject("Result");



                            //if no error in response
                            if (!json.getString("Message").equals("Errors")) {
                                //Toast.makeText(getApplicationContext(), json2.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response

                                //creating a new user object
//                                UserModel user = new UserModel(
//                                        userResult.getString("id"),
//                                        userResult.getString("email"),
//                                        userResult.getString("mobile"),
//                                        userResult.getString("userType")
                               // );


                                //storing the user in shared preferences
                                //SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                //Log.d("model", "onResponse: "+ user.toString());

                                //starting the profile activity
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(SignUpScreen.this,  SignInScreen.class));
                                finish();
                            } else {
                                JSONArray jsonArray = userResult.getJSONArray("errors");

                                JSONObject objectError = jsonArray.getJSONObject(0);
                                Toast.makeText(getApplicationContext(), objectError.getString("description"), Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
                Toast.makeText(getApplicationContext(), "Couldn't verify, please check your connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params2 = new HashMap<String, String>();
                params2.put("firstName", "");
                params2.put("lastName", "");
                params2.put("email", email);
                params2.put("password", password);
                params2.put("confirmPassword", password);
                params2.put("userType", "Rider");
                params2.put("mobile", phoneNumber);
                params2.put("smsCode", "");
                params2.put("city", "");
                params2.put("country", "");
                params2.put("uuid", "");

                return new JSONObject(params2).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomVolleyRequest.getInstance(getApplicationContext()).getRequestQueue().add(stringRequest);

    }

}