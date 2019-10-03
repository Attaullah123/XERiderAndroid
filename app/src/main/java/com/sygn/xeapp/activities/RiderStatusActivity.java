package com.sygn.xeapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sygn.xeapp.DemoActivity;
import com.sygn.xeapp.MainActivity;
import com.sygn.xeapp.R;
import com.sygn.xeapp.mapdierection.FetchURL;
import com.sygn.xeapp.mapdierection.TaskLoadedCallback;
import com.sygn.xeapp.model.LoginUserModel;
import com.sygn.xeapp.network.CustomVolleyRequest;
import com.sygn.xeapp.preferences.SharedPrefManager;
import com.sygn.xeapp.utility.Constants;
import com.sygn.xeapp.utility.LatLngInterpolator;
import com.sygn.xeapp.utility.MarkerAnimation;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RiderStatusActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, TaskLoadedCallback {
    private GoogleMap googleMap;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    public LinearLayout lay_yourride,lay_notification,lay_payment,lay_help,lay_setting,lay_wallet,lay_logout;
    private LinearLayout selectLoc;
    private TextView currentLoc,dropLocation;
    RelativeLayout myView,confirmPopupView,rideStartPopupView;
    // private RelativeLayout serviceLayout;
    private Context mContext;
    boolean isUp;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5445;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Marker currentLocationMarker;
    private Location currentLocation;
    private boolean firstTimeFlag = true;
    private String dropLatitude, dropLongitude,pickLatitude,pickLongitude,driverId,totalDistance,fare,userID,UUID,accessToken,requestId,
            rideStatus;
    private Geocoder geocoder;
    private List<Address> addresses;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private List<Place.Field> fields;
    private Place place;
    private ImageView openMenu;
    private PopupWindow mPopupWindow;
    private CardView selectionCard;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    private static final String TAG = "DemoActivity";
    private Polyline currentPolyline;
    private MarkerOptions place1, place2;
    private TextView timeStatus;
    String notificationID,launchURL;

    //private SlidingUpPanelLayout mLayout;
    int selectedPosition=-1;
    //private String startDesLat,startDesLong,endDesLat,endDesLong;

    private final View.OnClickListener clickListener = view -> {
        if (view.getId() == R.id.currentLocationImageButton && googleMap != null && currentLocation != null)
            animateCamera(currentLocation);
    };

    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult.getLastLocation() == null)
                return;
            currentLocation = locationResult.getLastLocation();
            if (firstTimeFlag && googleMap != null) {

                animateCamera(currentLocation);
                firstTimeFlag = false;
            }


            //showMarker(currentLocation);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_status_fragment);
        selectionCard = findViewById(R.id.loc_sec_cardview);
        confirmPopupView =  findViewById(R.id.confirm_popup);
        timeStatus = findViewById(R.id.confirm_time);
        rideStartPopupView = findViewById(R.id.ride_start_popup);
        currentLoc = findViewById(R.id.ride_current_loc);
        selectLoc = findViewById(R.id.select_loc);
        dropLocation = findViewById(R.id.drop_loc);
        progressBar = findViewById(R.id.progress_bar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        geocoder = new Geocoder(this, Locale.getDefault());
        requestId = getIntent().getStringExtra(Constants.RIDE_STATUS);
        Log.d("status", requestId);
        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.

        LoginUserModel user = SharedPrefManager.getInstance(this).getUser();
        userID = user.getUserId();
        Log.d("accessToken", user.getAccessToken());
        accessToken = user.getAccessToken();

        new Handler().postDelayed(new Runnable(){
            public void run() {
                String rideID = getIntent().getStringExtra("rideID");
                if(rideID != null){
                    //getRideStatus(rideID);
                }else {
                }
            }
        }, 0);

        UUID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserId", "");
        Log.d("preUserId", UUID);
        if (requestId!= null && accessToken!=null){
            getRideStatus(requestId);
        }
        isUp = false;



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
        findViewById(R.id.currentLocationImageButton).setOnClickListener(clickListener);
        Places.initialize(getApplicationContext(), "AIzaSyA8cuo-SKDhf6sKDbRKhAPyVyR-OtDxbgQ");
        // Create a new Places client instance.
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);
        place1 = new MarkerOptions().position(new LatLng(31.5203, 74.3517)).title("Location 2");
        place2 = new MarkerOptions().position(new LatLng(31.476068, 74.304428)).title("Location 2");

        selectLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).setCountry("pk").build(getApplicationContext());

                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });
        currentLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getRideStatus();
//             rideStartPopupView.setVisibility(View.VISIBLE);
//             confirmPopupView.setVisibility(View.INVISIBLE);
               // new FetchURL(RiderStatusActivity.this).execute(drawPolygon(place1.getPosition(), place2.getPosition(), "driving"), "driving");
            }
        });

        //vehicleLayout.setVisibility(View.VISIBLE);
                //  showConfirmPopup();
                //if (rideButton.getText())

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
                                getRideStatus(notificationID);
                            }

                        } else {
                            Log.d("LunchUrl", "Launch URL not found");
                            Uri data = Uri.parse(launchURL);

                            if(data != null){
                                notificationID = data.getQueryParameter("id");
                                Log.d("urlp action ", "onCreate: "+notificationID);
                                getRideStatus(notificationID);

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
                                getRideStatus(notificationID);

                            }

                        } else {
                            Log.d("LunchUrl", "Launch URL not found");
                            Uri data = Uri.parse(launchURL);

                            if(data != null){
                                notificationID = data.getQueryParameter("id");
                                Log.d("urlp action ", "onCreate: "+notificationID);
                                getRideStatus(notificationID);

                            }
                        }
                    }
                })
                .init();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.addMarker(place1);
        googleMap.addMarker(place2);
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        setToolbar();
        navigationSlide();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        invalidateOptionsMenu();

    }

    private String drawPolygon(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
       // https://maps.googleapis.com/maps/api/directions/json?origin=31.431803,74.35048999999&destination=31.522524,74.350342&mode=driving&key=AIzaSyA8cuo-SKDhf6sKDbRKhAPyVyR-OtDxbgQ
        return url;
    }

    private void getRideStatus(String requestId) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.getCache().clear();
        String GET_XERIDE_STATUS ="https://api.xeride.com/api/RideRequest/GetRideStatus?rideId="+requestId+"";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_XERIDE_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MapView", "status: " + response);

                try {
                    //getting the whole json object from the response
                    JSONObject json= (JSONObject) new JSONTokener(response).nextValue();
                    JSONObject userResult = json.getJSONObject("Result");
                    rideStatus = userResult.getString("rideStatus");

                    if (rideStatus.equalsIgnoreCase("Pending")){
                        progressBar.setVisibility(View.VISIBLE);

                    }else if(rideStatus.equalsIgnoreCase("Accept")){
                        confirmPopupView.setVisibility(View.VISIBLE);
                        timeStatus.setText("Accepted!");
                        timeStatus.setTypeface(null, Typeface.BOLD);
                        timeStatus.setTextColor(Color.parseColor("#205FA2"));
                        progressBar.setVisibility(View.GONE);

                    }else if(rideStatus.equalsIgnoreCase("Enroute")){
                        timeStatus.setText("4 mins away");
                        timeStatus.setTextColor(Color.parseColor("#000000"));
                        progressBar.setVisibility(View.GONE);
                        showNewPin(currentLocation);

                    }else if(rideStatus.equalsIgnoreCase("Arrive")){
                        timeStatus.setText("Arrived!");
                        timeStatus.setTextColor(Color.parseColor("#205FA2"));
                        timeStatus.setTypeface(null, Typeface.BOLD);
                        progressBar.setVisibility(View.GONE);
                        showNewPin(currentLocation);

                    }else if(rideStatus.equalsIgnoreCase("Start")){
                        rideStartPopupView.setVisibility(View.VISIBLE);
                        confirmPopupView.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);

                    }else if(rideStatus.equalsIgnoreCase("Finish")){
                        rideStartPopupView.setVisibility(View.INVISIBLE);
                        selectionCard.setVisibility(View.INVISIBLE);
                        progressBar.setVisibility(View.GONE);
                        showRatingPopUp();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Something went wrong please check your connection", Toast.LENGTH_LONG).show();
                        Log.d("MainA", "onResponse: " + error);
                    }
                }) { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " +accessToken);
                return params;
            }
        };

        // Add StringRequest to the RequestQueue
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = googleMap.addPolyline((PolylineOptions) values[0]);
    }

    private void updateRideStatus(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_XERIDE_STATUS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("heelo", "onResponse: "+response);
                confirmPopupView.setVisibility(View.VISIBLE);
                timeStatus.setText("Accepted!");
                timeStatus.setTypeface(null, Typeface.BOLD);
                timeStatus.setTextColor(Color.parseColor("#205FA2"));

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        //rideStartPopupView.setVisibility(View.VISIBLE);
                        timeStatus.setText("4 mins away");
                        timeStatus.setTextColor(Color.parseColor("#000000"));

                        showNewPin(currentLocation);
                        //updateRideStatus();
                    }
                }, 2000);

                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        timeStatus.setText("Arrived!");
                        timeStatus.setTextColor(Color.parseColor("#205FA2"));
                        timeStatus.setTypeface(null, Typeface.BOLD);
                        showNewPin(currentLocation);
                        //updateRideStatus();
                    }
                }, 10000);

                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        rideStartPopupView.setVisibility(View.VISIBLE);
                        confirmPopupView.setVisibility(View.INVISIBLE);
                        //updateRideStatus();
                    }
                }, 12000);

                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        rideStartPopupView.setVisibility(View.INVISIBLE);
                        selectionCard.setVisibility(View.INVISIBLE);
                        showRatingPopUp();
                        //updateRideStatus();
                    }
                }, 20000);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", error.toString());
                Toast.makeText(getApplicationContext(), "Couldn't verify, please check your connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("rideRequestId", requestId);
                params.put("rideStatus", rideStatus);
                params.put("userId", userID);

                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " +accessToken);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomVolleyRequest.getInstance(getApplicationContext()).getRequestQueue().add(stringRequest);

    }

    private void showRatingPopUp(){
        LayoutInflater mLayoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View mView = mLayoutInflater.inflate(R.layout.rating_popup, null);
        mPopupWindow = new PopupWindow(RiderStatusActivity.this);
        mPopupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setContentView(mView);
        RatingBar ratingBar = mView.findViewById(R.id.ratingBar);
        //RelativeLayout mBtnCancel =  mView.findViewById(R.id.cancel_layout);
//        RelativeLayout mainLayout = mView.findViewById(R.id.main_layout);
//        mainLayout.setAlpha(0.5F);
        // mBtnCancel.setOnClickListener(this);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Handler handler = new Handler();
                //txtRatingValue.setText(String.valueOf(rating));
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        Toast.makeText(getApplicationContext(),"Thanks for riding with XE-RIDE", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        //updateRideStatus();
                    }
                }, 1500);

            }
        });
        Drawable d = new ColorDrawable(Color.WHITE);

        d.setAlpha(130);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setOutsideTouchable(false);

        mPopupWindow.showAtLocation(mView, Gravity.NO_GRAVITY, 0, 0);
        getWindow().setBackgroundDrawable(d);
    }


    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //locationRequest.setInterval(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RiderStatusActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                return;
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }


    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status)
            return true;
        else {
            if (googleApiAvailability.isUserResolvableError(status))
                Toast.makeText(this, "Please Install google play services to use this application", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                Toast.makeText(this, "Permission denied by uses", Toast.LENGTH_SHORT).show();
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startCurrentLocationUpdates();
        }
    }

    private void animateCamera(@NonNull Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)));

        dropLatitude = String.valueOf(location.getLatitude());
        dropLongitude = String.valueOf(location.getLongitude());
        Log.d("latt", dropLatitude +"..."+ dropLongitude);
        //       String currentLocation1 = getCurrentLocationViaJSON(location.getLatitude(), location.getLongitude());
//        System.out.println("address"+ currentLocation1);
    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
    }

    private void showMarker(@NonNull Location currentLocation) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        if (currentLocationMarker == null) {
            currentLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)).position(latLng));


            // Log.d("address", currentLocation1);
            try {
                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                String address = addresses.get(0).getAddressLine(0);
                String address1 = addresses.get(0).getFeatureName();
                String address2 = addresses.get(0).getAdminArea();
                Log.d("address", address1+" "+ address2);
                currentLoc.setText(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());

    }

    private void showNewPin(@NonNull Location currentLocation) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        if (currentLocationMarker == null) {
            currentLocationMarker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon1)).position(latLng).title("Your XE-RIDE is here"));
            currentLocationMarker.showInfoWindow();


            // Log.d("address", currentLocation1);
            try {
                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                String address = addresses.get(0).getAddressLine(0);
                String address1 = addresses.get(0).getFeatureName();
                String address2 = addresses.get(0).getAdminArea();
                Log.d("address", address1+" "+ address2);
                currentLoc.setText(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            MarkerAnimation.animateMarkerToGB(currentLocationMarker, latLng, new LatLngInterpolator.Spherical());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                place = Autocomplete.getPlaceFromIntent(data);
                Log.i("hello", "Place: " + place.getName() + ", " + place.getLatLng());
                LatLng lati = place.getLatLng();
                pickLatitude = String.valueOf(lati.latitude);
                pickLongitude = String.valueOf(lati.longitude);
                dropLocation.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("status", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (fusedLocationProviderClient != null)
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            startCurrentLocationUpdates();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient = null;
        googleMap = null;
    }

    public void navigationSlide(){
        lay_yourride = findViewById(R.id.your_ride);
        lay_payment = findViewById(R.id.payment);
        lay_notification = findViewById(R.id.notification);
        lay_help = findViewById(R.id.help);
        lay_setting = findViewById(R.id.settings);
        lay_wallet = findViewById(R.id.wallet);

        lay_yourride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderStatusActivity.this, UserTripHistory.class);
                startActivity(intent);
            }
        });
        lay_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderStatusActivity.this, PaymentScreen.class);
                startActivity(intent);
            }
        });
        lay_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RiderStatusActivity.this, SettingScreen.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(Gravity.LEFT); //OPEN Nav Drawer!
        }else {
            finish();

        }
    }
    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        openMenu = findViewById(R.id.navigation_menu);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("");

        openMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (drawer.isDrawerOpen(navigationView)) {
                    drawer.closeDrawer(navigationView);
                } else {
                    drawer.openDrawer(navigationView);
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
