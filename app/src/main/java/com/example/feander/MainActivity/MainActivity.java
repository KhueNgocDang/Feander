package com.example.feander.MainActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.feander.R;
import com.example.feander.MainActivity.MainFragments.LocationFragment;
import com.example.feander.MainActivity.MainFragments.SearchFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    LatLng latLng;
    Location location;
    double latitude;
    double longitude;
    LocationFragment locationFragment;
    SearchFragment searchFragment;
    Intent map_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (network_enabled) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(location!=null){
                latitude  = location.getLatitude();
                longitude = location.getLongitude();
            }
            latLng = new LatLng(latitude,longitude);
        }

        searchFragment = SearchFragment.newInstance(latitude,longitude);
        locationFragment = LocationFragment.newInstance(latitude,longitude);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, locationFragment)
                .commit();
        getSupportFragmentManager().beginTransaction().detach(locationFragment).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,searchFragment)
                .commit();

        map_intent = new Intent(this, MapsActivity.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.location_info:
                            getSupportFragmentManager().beginTransaction().detach(searchFragment).commit();
                            getSupportFragmentManager().beginTransaction().attach(locationFragment).commit();
                            break;
                        case R.id.map_location:
                            startActivity(map_intent);
                            break;
                        case R.id.action_search:
                            getSupportFragmentManager().beginTransaction().detach(locationFragment).commit();
                            getSupportFragmentManager().beginTransaction().attach(searchFragment).commit();
                    }
                    return true;
                }
            };
}