package com.sygn.xeapp.registration;

import android.content.Intent;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sygn.xeapp.MainActivity;
import com.sygn.xeapp.R;
import com.sygn.xeapp.model.LoginUserModel;
import com.sygn.xeapp.network.CustomVolleyRequest;
import com.sygn.xeapp.preferences.SharedPrefManager;
import com.sygn.xeapp.utility.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SignInScreen extends AppCompatActivity {
    private static final String TAG = "";
    private EditText inputPhone, inputPassword;

    private ProgressBar progressBar;
    private Button signIn;
    private TextView signUpNnow;
    private ImageView signInGoogle;
    private final static int RC_SIGN_IN = 123;
    private String CheckUUID,UUID;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListner);
//
//    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mAuth = FirebaseAuth.getInstance();
//
//        //check the current user
//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(SignInScreen.this, MainActivity.class));
//            finish();
//        }
        setContentView(R.layout.sign_in_layout);
        inputPhone = (EditText) findViewById(R.id.sign_editPhone);
        inputPassword = (EditText) findViewById(R.id.sign_editPassword);
        progressBar = findViewById(R.id.progressBar);
        signIn = (Button) findViewById(R.id.sign_in);
        signInGoogle = findViewById(R.id.gmail_signin);
        signUpNnow = (TextView) findViewById(R.id.txt_signup);


        UUID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserId", "");
        Log.d("preUserId", UUID);
//        if (CheckUUID == null || CheckUUID.isEmpty()){
//            UUID = "";
//
//        }else {
//            UUID = CheckUUID;
//           // Log.d("UUID==", UUID);
//        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        signUpNnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInScreen.this, SignUpScreen.class));
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignInScreen.this, MainActivity.class));

                    riderLogin();

            }
        });
    }
    public void riderLogin(){
        progressBar.setVisibility(View.VISIBLE);
        final String phone = inputPhone.getText().toString();
        final String password = inputPassword.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            inputPhone.setError("Please enter phoneNumber");
            inputPhone.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Enter Password");
            inputPassword.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        String url = "https://auth.xeride.com/connect/token";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);

                try {
                    JSONObject obj = new JSONObject(response);
                   // String token = obj.getString("access_token");
                    //String UUID = obj.getString("UUID");


                    //creating a new user object
                    LoginUserModel user = new LoginUserModel(
                            obj.getString("userId"),
                            password,
                            phone,
                            obj.getString("access_token")

                     );

                   // storing the user in shared preferences
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                    Log.d("model", "onResponse: "+ user.toString());


                    //starting the profile activity
                    startActivity(new Intent(SignInScreen.this,  MainActivity.class));
                    finish();
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Please enter correct PhoneNumber and Password",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR","error => "+error.toString());
                Toast.makeText(getApplicationContext(),"Please enter correct PhoneNumber and Password",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        }
        ) {
            // this is the relevant method
            @Override
            public byte[] getBody() throws AuthFailureError {
                String httpPostBody="grant_type=password&username="+phone+"&password="+password+"&client_id=native.code&client_secret=SomeValue&scope=Xeride.API openid&uuid="+UUID+"";
                // usually you'd have a field with some values you'd want to escape, you need to do it yourself if overriding getBody. here's how you do it
                try {
                    httpPostBody=httpPostBody+"&randomFieldFilledWithAwkwardCharacters="+ URLEncoder.encode("{{%stuffToBe Escaped/","UTF-8");
                } catch (UnsupportedEncodingException exception) {
                    Log.e("ERROR", "exception", exception);
                    // return null and don't pass any POST string if you encounter encoding error
                    progressBar.setVisibility(View.GONE);
                    return null;
                }
                return httpPostBody.getBytes();
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomVolleyRequest.getInstance(getApplicationContext()).getRequestQueue().add(stringRequest);
    }

}
