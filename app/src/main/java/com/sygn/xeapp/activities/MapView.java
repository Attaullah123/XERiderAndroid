package com.sygn.xeapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.navigation.NavigationView;
import com.sygn.xeapp.R;
import com.sygn.xeapp.adapter.CabListAdapter;
import com.sygn.xeapp.model.CabListModel;
import com.sygn.xeapp.model.LoginUserModel;
import com.sygn.xeapp.network.CustomVolleyRequest;
import com.sygn.xeapp.preferences.SharedPrefManager;
import com.sygn.xeapp.registration.SignInScreen;
import com.sygn.xeapp.registration.SignUpScreen;
import com.sygn.xeapp.utility.Constants;
import com.sygn.xeapp.utility.LatLngInterpolator;
import com.sygn.xeapp.utility.MarkerAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    private GoogleMap googleMap;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<CabListModel> cabArrayList;
    private CabListAdapter cabListAdapter;
    private CabListModel cabListModel;
    public  LinearLayout lay_yourride,lay_notification,lay_payment,lay_help,lay_setting,lay_wallet,lay_logout;
    private LinearLayout selectLoc;
    private Button rideButton;
    private JSONObject jsonObj;
    private TextView currentLoc,dropLocation;
    RelativeLayout myView,confirmPopupView;
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

            showMarker(currentLocation);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_main_menu);
        myView = findViewById(R.id.vehicle_list);
        selectionCard = findViewById(R.id.loc_sec_cardview);
        rideButton = findViewById(R.id.ride_booking);
        currentLoc = findViewById(R.id.ride_current_loc);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view1);
        selectLoc = findViewById(R.id.select_loc);
        dropLocation = findViewById(R.id.drop_loc);
        progressBar = findViewById(R.id.progress_bar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        geocoder = new Geocoder(this,Locale.getDefault());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //getXeRideSubService();
        progressDialog = new ProgressDialog(MapView.this);
        myView.setVisibility(View.INVISIBLE);
        rideButton.setText("Book My Ride");
        isUp = false;

        LoginUserModel user = SharedPrefManager.getInstance(this).getUser();
        userID = user.getUserId();
        Log.d("accessToken", user.getAccessToken());
        accessToken = user.getAccessToken();

        UUID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserId", "");
        Log.d("preUserId", UUID);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
        findViewById(R.id.currentLocationImageButton).setOnClickListener(clickListener);
        Places.initialize(getApplicationContext(), "AIzaSyA8cuo-SKDhf6sKDbRKhAPyVyR-OtDxbgQ");
        // Create a new Places client instance.
        fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);

        selectLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).setCountry("pk").build(getApplicationContext());

                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        rideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dropLoc = String.valueOf(dropLocation.getText());
                //Log.d("onClick: ", dropLoc);
                if (dropLoc.equalsIgnoreCase("Select drop location")){
                    Toast.makeText(getApplicationContext(),"Select your Drop-UP Location",Toast.LENGTH_LONG).show();
                }else {
                    String rideStatus = String.valueOf(rideButton.getText());
                    if (rideStatus.equalsIgnoreCase("Book My Ride")){
                        sendUserLocation();
                        onSlideViewButtonClick(view);
                    }else {
                        //confirmPopupView.setVisibility(View.VISIBLE);
                        selectionCard.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.INVISIBLE);
//                        rideButton.setVisibility(View.INVISIBLE);
                        //onSlideViewButtonClick(view);
                        confirmRide();
                    }
                }

                //vehicleLayout.setVisibility(View.VISIBLE);
              //  showConfirmPopup();
                //if (rideButton.getText())


            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

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

    public void sendUserLocation(){
        JSONObject jsonMainBody = new JSONObject();
        try {
            //jsonMainBody.put("userType","Administrator");
            jsonMainBody.put("service","Bike");
            jsonMainBody.put("serviceId","1");

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("latitude", pickLatitude);
            jsonObject1.put("longitude", pickLongitude);

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("latitude", dropLatitude);
            jsonObject2.put("longitude", dropLongitude);

            jsonMainBody.put("pickupLocation",jsonObject1);
            jsonMainBody.put("dropoffLocation",jsonObject2);

            Log.d("jsonBody", jsonMainBody.toString());

            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Constants.FIND_XESERVICES, jsonMainBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("getService", response.toString());
                            try{


                                //we have the array named hero inse the object
                                //so here we are getting that json array
                                JSONArray xeListSubArray = response.getJSONArray("Result");
                                cabArrayList = new ArrayList<CabListModel>();

                                for (int i = 0; i < xeListSubArray.length(); i++) {
                                    JSONObject xeObject = xeListSubArray.getJSONObject(i);
                                    cabListModel = new CabListModel();

                                    // String url ="http://ec2-18-218-128-242.us-east-2.compute.amazonaws.com/";
                                    // final String imageUrl = url.concat(xeObject.getString("imageUrl"));

                                    //cabListModel.setCabImage(xeObject.getString("imageUrl"));
                                    cabListModel.setCabName(xeObject.getString("serviceType"));
                                    cabListModel.setCabPrice(xeObject.getString("fare"));
                                    cabListModel.setCabTime(xeObject.getString("time"));
                                    cabListModel.setDriverId(xeObject.getString("driverId"));
                                    //driverId = xeObject.getString("driverId");
                                    totalDistance = xeObject.getString("distanceTime");


                                    //Log.d("services", fare +" "+ driverId +" "+ totalDistance);
                                    cabArrayList.add(cabListModel);
                                }
                                cabListAdapter = new CabListAdapter(getApplicationContext(), cabArrayList, new CabListAdapter.ServicesListAdapterListener() {
                                    @Override
                                    public void cardViewViewOnClick(View v, int position) {

                                        rideButton.setText("Confirm Ride");
                                        driverId = cabArrayList.get(position).getDriverId();
                                        fare = cabArrayList.get(position).getCabPrice();
                                        //Toast.makeText(getApplicationContext(),cabArrayList.get(position).getDriverId(),Toast.LENGTH_LONG).show();
                                    }
                                });
                                recyclerView.setAdapter(cabListAdapter);
                                cabListAdapter.notifyDataSetChanged();

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Couldn't get services, please check connection", Toast.LENGTH_SHORT).show();
                    Log.d("Error", error.toString());

                }
            }){ //no semicolon or coma
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization","Bearer "+accessToken);
                    return params;
                }
            };
            objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    Constants.socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomVolleyRequest.getInstance(getApplicationContext()).getRequestQueue().add(objectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void confirmRide(){

        JSONObject jsonMainBody = new JSONObject();
        try {
            //jsonMainBody.put("userType","Administrator");
            jsonMainBody.put("serviceType","Bike");
            jsonMainBody.put("requestId","0");
            jsonMainBody.put("riderId",userID);
            jsonMainBody.put("driverId",driverId);
            jsonMainBody.put("uuid",UUID);
            jsonMainBody.put("totalDistance","0");
            jsonMainBody.put("totalFare",fare);


            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("latitude", pickLatitude);
            jsonObject1.put("longitude", pickLongitude);

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("latitude", dropLatitude);
            jsonObject2.put("longitude", dropLongitude);

            jsonMainBody.put("pickupLocation",jsonObject1);
            jsonMainBody.put("dropoffLocation",jsonObject2);

            Log.d("jsonBody", jsonMainBody.toString());
            JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, Constants.Confirm_Ride_StepOne, jsonMainBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("getCofirm", response.toString());

                            try {
                                JSONObject jsonObject = response.getJSONObject("Result");
                                requestId =  jsonObject.getString("requestId");
                                Log.d("requestId", requestId);
                                if (requestId !=null){
                                    //getRideStatus();
                                    Intent intent = new Intent(getApplicationContext(),RiderStatusActivity.class);
                                    intent.putExtra(Constants.RIDE_STATUS, requestId);
                                    startActivity(intent);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Couldn't register  , please check connection", Toast.LENGTH_SHORT).show();
                    Log.d("Error", error.toString());

                }
            }){ //no semicolon or coma
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    params.put("Authorization","Bearer "+accessToken);
                    return params;
                }
            };
            objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    Constants.socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            CustomVolleyRequest.getInstance(getApplicationContext()).getRequestQueue().add(objectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void startCurrentLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //locationRequest.setInterval(3000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapView.this,
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



    // slide the view from below itself to the current position
    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        selectionCard.setVisibility(View.INVISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void onSlideViewButtonClick(View view) {
        if (isUp) {
             slideDown(myView);
          //   myView.setVisibility(View.INVISIBLE);
            //rideButton.setText("Book My Ride");

        } else {
            slideUp(myView);
            //rideButton.setText("Select Cab");

        }
        isUp = !isUp;
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
                Intent intent = new Intent(MapView.this, UserTripHistory.class);
                startActivity(intent);
            }
        });
        lay_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapView.this, PaymentScreen.class);
                startActivity(intent);
            }
        });
        lay_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapView.this, SettingScreen.class);
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

