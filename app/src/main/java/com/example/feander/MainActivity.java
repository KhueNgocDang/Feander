package com.example.feander;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.feander.User.data.Repository;
import com.example.feander.User.ui.user.LoginActivity;
import com.example.feander.User.ui.user.SavedLocationActivity;
import com.example.feander.User.ui.user.UpdateActivity;
import com.example.feander.ui.MainActivityFragment.LocationFragment;
import com.example.feander.ui.MainActivityFragment.SearchFragment;
import com.example.feander.ui.MainActivityFragment.User_Fragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    LatLng latLng;
    Location location;
    private String userName, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double latitude = 0;
        double longitude = 0;
        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        userName = getIntent().getStringExtra("userName");
        userId = getIntent().getStringExtra("id");
//        Log.d("userId", userId);

        if (network_enabled) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            latLng = new LatLng(latitude, longitude);
        }

        final LocationFragment locationFragment = LocationFragment.newInstance(latitude, longitude);
        locationFragment.setUserId(userId);
        locationFragment.setCallingActivty(this);
        final SearchFragment searchFragment = SearchFragment.newInstance(latitude, longitude);
        final User_Fragment user_fragment = new User_Fragment(userName);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, locationFragment)
                .commit();
        getSupportFragmentManager().beginTransaction().detach(locationFragment).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, searchFragment)
                .commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, user_fragment)
                .commit();
        getSupportFragmentManager().beginTransaction().detach(user_fragment).commit();

        final Intent map_intent = new Intent(this, MapsActivity.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);
        BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new
                BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.location_info:
                                getSupportFragmentManager().beginTransaction().detach(searchFragment).commit();
                                getSupportFragmentManager().beginTransaction().attach(locationFragment).commit();
                                getSupportFragmentManager().beginTransaction().detach(user_fragment).commit();

                                break;
                            case R.id.map_location:
                                startActivity(map_intent);
                                break;
                            case R.id.action_search:
                                getSupportFragmentManager().beginTransaction().detach(locationFragment).commit();
                                getSupportFragmentManager().beginTransaction().attach(searchFragment).commit();
                                getSupportFragmentManager().beginTransaction().detach(user_fragment).commit();
                                break;
                            case R.id.user:
                                getSupportFragmentManager().beginTransaction().detach(searchFragment).commit();
                                getSupportFragmentManager().beginTransaction().detach(locationFragment).commit();
                                getSupportFragmentManager().beginTransaction().attach(user_fragment).commit();
                                break;
                        }
                        return true;
                    }
                };
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
    }

    public void updateUser(View view) {
        if (userName != null) {
            startActivity(new Intent(this, UpdateActivity.class).putExtra("userName", userName).putExtra("userId", userId));
        } else {
            Toast.makeText(this, "Bạn đang xem với tư cách khách", Toast.LENGTH_LONG).show();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logOut(View view) {
        Repository repository = new Repository();
        repository.setContext(getApplicationContext());
        repository.logOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void showSavedLocation(View view) {
        if (userName != null)
            startActivity(new Intent(this, SavedLocationActivity.class).putExtra("userId", userId));
        else
            Toast.makeText(this, "Bạn đang xem với tư cách khách", Toast.LENGTH_LONG).show();

    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public void logOut(MenuItem item) {
//        Repository repository = new Repository();
//        repository.setContext(getApplicationContext());
//        repository.logOut();
//        startActivity(new Intent(this, LoginActivity.class));
//        finish();
//    }
}