package com.sygn.xeapp.registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sygn.xeapp.R;
import com.sygn.xeapp.network.CustomVolleyRequest;
import com.sygn.xeapp.preferences.SharedPrefManager;
import com.sygn.xeapp.utility.Constants;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

public class EnterPhoneScreen extends AppCompatActivity {
    private Button signUp;
    private EditText enterPhoneNum;
    private SharedPrefManager session;
    private String smsCode,phonenumber;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_phone_layout);
        signUp = (Button) findViewById(R.id.sign_up);
        enterPhoneNum = findViewById(R.id.reg_editPhoneNumber);
        progressBar = findViewById(R.id.progressBar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//            Intent intent = new Intent(WelcomeScreen.this, MapView.class);
                registerPhoneNum();



            }
        });
        session = new SharedPrefManager(getApplicationContext());

    }

    public void registerPhoneNum() {
        progressBar.setVisibility(View.VISIBLE);
        phonenumber = enterPhoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(phonenumber)) {
            enterPhoneNum.setError("Please enter your Mobile Number");
            //enterPhoneNum.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        StringRequest jsonRequest = new StringRequest(Request.Method.POST, Constants.USER_REG_NUMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resss", response);

                try {
                    JSONObject json= (JSONObject) new JSONTokener(response).nextValue();
                    JSONObject json2 = json.getJSONObject("Result");
                    smsCode = json2.getString("smsCode");
                    phonenumber = json2.getString("phoneNumber");
                    if (smsCode!=null){
                        startActivity(new Intent(EnterPhoneScreen.this,  VerifyPhoneNumber.class));
                        finish();
                        //progressDialog.dismiss();
                        session.userPhoneCode(smsCode,phonenumber);
                    }else {
                        //progressDialog.dismiss();
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
                params2.put("phone", phonenumber);

                return new JSONObject(params2).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomVolleyRequest.getInstance(getApplicationContext()).getRequestQueue().add(jsonRequest);

    }

}


