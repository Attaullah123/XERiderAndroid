package com.sygn.xeapp.activities;

import android.content.Intent;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.sygn.xeapp.MainActivity;
import com.sygn.xeapp.R;
import com.sygn.xeapp.registration.EnterPhoneScreen;
import com.sygn.xeapp.registration.SignInScreen;
import com.sygn.xeapp.registration.WelcomeScreen;

public class SplashScreen extends AppCompatActivity {

    private ImageView animImage;
    private ImageView cabTitle;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        animImage = findViewById(R.id.cab_logo);
        cabTitle = findViewById(R.id.cab_title);



        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        animImage.setAnimation(animation);
       // textView.setAnimation(animation);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug", "User:" + userId);
                if (userId != null){
                    splash();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("UserId", userId).apply();
                }
                    Log.d("debug", "registrationId:" + registrationId);

            }
        });

    }

    private void splash(){
        intent = new Intent(getApplicationContext(), WelcomeScreen.class);

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(1000);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };

        thread.start();
    }
}
