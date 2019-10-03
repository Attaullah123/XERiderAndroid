package com.sygn.xeapp.registration;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sygn.xeapp.MainActivity;
import com.sygn.xeapp.R;
import com.sygn.xeapp.preferences.SharedPrefManager;

public class WelcomeScreen extends AppCompatActivity {
    private Button btnSignIn,btnCreateAccount;
    private SharedPrefManager session;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        btnSignIn = findViewById(R.id.wel_sign_in);
        btnCreateAccount = findViewById(R.id.wel_create_account);
        session = new SharedPrefManager(getApplicationContext());

        if (session.isLoggedIn()){
            startActivity(new Intent(WelcomeScreen.this, MainActivity.class));

            finish();
        }



        btnSignIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(WelcomeScreen.this, SignInScreen.class);
//            Intent intent = new Intent(WelcomeScreen.this, MapView.class);
            startActivity(intent);
        }
    });
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreen.this, EnterPhoneScreen.class);
                startActivity(intent);
            }
        });

    }
}
