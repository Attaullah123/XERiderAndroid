package com.sygn.xeapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.sygn.xeapp.activities.RiderStatusActivity;


public class MyApplication extends Application {
    private Context context;
    String notificationID,launchURL;
    @Override
    public void onCreate() {
        super.onCreate();

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


//        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
//        status.getPermissionStatus().getEnabled();
//        String userID = status.getSubscriptionStatus().getUserId();


        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationReceivedHandler(new OneSignal.NotificationReceivedHandler() {
                    @Override
                    public void notificationReceived(OSNotification notification) {
                        launchURL = notification.payload.launchURL;

                        if (launchURL != null) {
                            Log.d("LunchUrl", "Launch URL: " + launchURL);
                            Uri data = Uri.parse(launchURL);

                            if(data != null){
                                notificationID = data.getQueryParameter("id");
                                Log.d("urlp action ", "onCreate: "+notificationID);
                                loadRideView(notificationID);
                            }

                        } else {
                            Log.d("LunchUrl", "Launch URL not found");
                            Uri data = Uri.parse(launchURL);

                            if(data != null){
                                notificationID = data.getQueryParameter("id");
                                Log.d("urlp action ", "onCreate: "+notificationID);
                                loadRideView(notificationID);

                            }

                        }
                    }
                })
                .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenResult result) {

                        launchURL = result.notification.payload.launchURL;

                        if (launchURL != null) {
                            Log.d("LunchUrl", "Launch URL: " + launchURL);
                            Uri data = Uri.parse(launchURL);
                            if(data != null){
                                notificationID = data.getQueryParameter("id");
                                Log.d("urlp action ", "onCreate: "+notificationID);
                                loadRideView(notificationID);

                            }

                        } else {
                            Log.d("LunchUrl", "Launch URL not found");
                            Uri data = Uri.parse(launchURL);

                            if(data != null){
                                notificationID = data.getQueryParameter("id");
                                Log.d("urlp action ", "onCreate: "+notificationID);
                                loadRideView(notificationID);

                            }
                        }
                    }
                })
                .init();


        // String userID = status.getSubscriptionStatus().getUserId();
        //String id= OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
        // PreferenceManager.getDefaultSharedPreferences(this).edit().putString("UserId", userID).apply();
    }
    public void loadRideView(String rideID){
        Intent intent = new Intent(getApplicationContext(), RiderStatusActivity.class);
        intent.putExtra("rideID",rideID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }
}
