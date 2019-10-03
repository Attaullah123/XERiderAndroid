package com.sygn.xeapp.registration;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.sygn.xeapp.R;
import com.sygn.xeapp.preferences.SharedPrefManager;


import java.util.HashMap;

public class VerifyPhoneNumber extends AppCompatActivity {
    private Toolbar toolbar;
    TextView title;
    private String verifyCode;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_number);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        title = findViewById(R.id.bar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("Phone Verification");
        Pinview pinview1 = findViewById(R.id.pinview1);
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        HashMap<String, String> user = sharedPrefManager.getPhoneCode();
        verifyCode = user.get(SharedPrefManager.KEY_SMS_CODE);
        Log.d("smms", verifyCode);
        //verifyCode = "0000";
        Log.d("pinn", verifyCode);

        View.OnClickListener onClickListener = view -> {

        };
        pinview1.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Toast.makeText(VerifyPhoneNumber.this, pinview.getValue(), Toast.LENGTH_SHORT).show();

                if (verifyCode.equals(pinview.getValue())){
                    Toast.makeText(VerifyPhoneNumber.this, "Number verify successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(VerifyPhoneNumber.this,SignUpScreen.class));
                    finish();
                }else {
                    Toast.makeText(VerifyPhoneNumber.this, "Enter code is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
