package com.example.feander;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.feander.DetailedActivity.DetailedFragments.DetailedDescriptionFragment;
import com.example.feander.ui.MainActivityFragment.LocationFragment;
import com.example.feander.ui.MainActivityFragment.SearchFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    LatLng latLng;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double latitude = 0;
        double longitude = 0;
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
        
        new LocationFragment();
        final LocationFragment locationFragment = LocationFragment.newInstance(latitude,longitude);

        new SearchFragment();
        final SearchFragment searchFragment = SearchFragment.newInstance(latitude,longitude);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, locationFragment)
                .commit();
        getSupportFragmentManager().beginTransaction().detach(locationFragment).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,searchFragment)
                .commit();

        final Intent map_intent = new Intent(this,MapsActivity.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);
        BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new
                BottomNavigationView.OnNavigationItemSelectedListener() {
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
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
    }

}