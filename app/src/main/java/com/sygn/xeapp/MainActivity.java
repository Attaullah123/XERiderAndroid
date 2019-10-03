package com.sygn.xeapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sygn.xeapp.activities.MapView;
import com.sygn.xeapp.activities.PaymentScreen;
import com.sygn.xeapp.activities.SettingScreen;
import com.sygn.xeapp.activities.UserPackagesScreen;
import com.sygn.xeapp.activities.UserTripHistory;
import com.sygn.xeapp.activities.WalletScreen;
import com.sygn.xeapp.adapter.MianMenuAdapter;
import com.sygn.xeapp.fragments.PackRideFragment;
import com.sygn.xeapp.model.LoginUserModel;
import com.sygn.xeapp.model.MainMenuModel;
import com.sygn.xeapp.model.UserModel;
import com.sygn.xeapp.network.CustomVolleyRequest;
import com.sygn.xeapp.preferences.SharedPrefManager;
import com.sygn.xeapp.registration.SignInScreen;
import com.sygn.xeapp.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView recyclerView;
    private ArrayList<MainMenuModel> menuModelArrayList;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private MianMenuAdapter mianMenuAdapter;
    private ImageView openMenu;
    public LinearLayout lay_yourride,lay_notification,lay_payment,lay_help,lay_setting,lay_wallet,lay_logout,layout_packages;
    private ProgressBar progressBar;
    private String accessToken,userPassword,userPhone,UUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//               toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        //toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationInit();

        recyclerView =  findViewById(R.id.recycle_view);
        menuModelArrayList = new ArrayList<>();
        progressBar = findViewById(R.id.progress_bar);

        LoginUserModel user = SharedPrefManager.getInstance(this).getUser();
        Log.d("userID", user.getUserId());
        Log.d("accessToken", user.getAccessToken());
        Log.d("password", user.getUserPassword());
        Log.d("phone", user.getUserPhone());
        accessToken = user.getAccessToken();
        userPassword = user.getUserPassword();
        userPhone = user.getUserPhone();

//        UUID = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("UserId", "");
//        Log.d("preUserId", UUID);
//        if (UUID == null || UUID.isEmpty()){
//
//        }else {
//           riderLogin();
//
//        }

        if (accessToken!=null){
            getResponse();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void navigationInit(){
        drawer = findViewById(R.id.drawer_layout);
        navigationView =  findViewById(R.id.navigation_view);
        openMenu = findViewById(R.id.navigation_menu);

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

    private void setToolbar() {
        toolbar =  findViewById(R.id.toolbar);
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

    public void navigationSlide(){
        lay_yourride = findViewById(R.id.your_ride);
        lay_payment = findViewById(R.id.payment);
        lay_notification = findViewById(R.id.notification);
        lay_help = findViewById(R.id.help);
        lay_setting = findViewById(R.id.settings);
        lay_wallet = findViewById(R.id.wallet);
        layout_packages = findViewById(R.id.packages);

        lay_yourride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserTripHistory.class);
                startActivity(intent);
            }
        });
        lay_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PaymentScreen.class);
                startActivity(intent);
            }
        });
        lay_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingScreen.class);
                startActivity(intent);
            }
        });
        lay_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WalletScreen.class);
                startActivity(intent);
            }
        });
        layout_packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserPackagesScreen.class);
                startActivity(intent);
            }
        });
    }

    public void riderLogin(){

        String url = "https://auth.xeride.com/connect/token";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("Response", response);


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
                String httpPostBody="grant_type=password&username="+userPhone+"&password="+userPassword+"&client_id=native.code&client_secret=SomeValue&scope=Xeride.API openid&uuid="+UUID+"";
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

    public void getResponse(){
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.GET_XERIDE_LIST, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MainActivity", "onResponse: " + response);


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray xelistArray = obj.getJSONArray("Result");
                            Constants.getContstantInstance().setJsonArray(xelistArray);

                            //now looping through all the elements of the json array
                            for (int i = 0; i < xelistArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject xeObject = xelistArray.getJSONObject(i);
                                MainMenuModel mainMenuModel = new MainMenuModel();

                                //String url ="http://ec2-18-218-128-242.us-east-2.compute.amazonaws.com/";
                               // final String imageUrl = url.concat(xeObject.getString("imageUrl"));

                                mainMenuModel.setImageUrl(xeObject.getString("imageUrl"));
                                mainMenuModel.setName(xeObject.getString("name"));
                                menuModelArrayList.add(mainMenuModel);
                            }
                            mianMenuAdapter = new MianMenuAdapter(getApplicationContext(), menuModelArrayList);
                            recyclerView.setAdapter(mianMenuAdapter);
                            progressBar.setVisibility(View.INVISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when get error
                        // Snackbar.make(mCLayout, "Error.", Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"Something went wrong please check your connection",Toast.LENGTH_LONG).show();
                        Log.d("MainA", "onResponse: "+error);
                        progressBar.setVisibility(View.INVISIBLE);
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

        // Add StringRequest to the RequestQueue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constants.socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomVolleyRequest.getInstance(getApplicationContext()).getRequestQueue().add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.sign_out:
                Toast.makeText(MainActivity.this, "Sign Out", Toast.LENGTH_SHORT).show();
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                //mAuth.signOut();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
