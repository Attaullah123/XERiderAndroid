package com.sygn.xeapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sygn.xeapp.R;
import com.sygn.xeapp.registration.WelcomeScreen;

public class SettingScreen extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView titleBar;
    private ImageView pressBack;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        pressBack=findViewById(R.id.back_btn);
        titleBar = findViewById(R.id.bar_title);
        linearLayout = findViewById(R.id.save_contact);
        titleBar.setText("Settings");
        pressBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingScreen.this, ContactSaveScreen.class);
                startActivity(intent);
            }
        });

    }
}
