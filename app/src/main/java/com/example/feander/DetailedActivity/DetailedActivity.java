package com.example.feander.DetailedActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.feander.DetailedActivity.DetailedFragments.DetailedDescriptionFragment;
import com.example.feander.DetailedActivity.DetailedFragments.DetailedHighlightFragment;
import com.example.feander.DetailedActivity.DetailedFragments.DetailedInfoFragment;
import com.example.feander.Location.LocationModel;
import com.example.feander.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailedActivity extends AppCompatActivity {

    LatLng latLng;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

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
        }

        //Get intent from MainActivity
        Intent intent = getIntent();
        final LocationModel locationModel = intent.getParcelableExtra("Location");

        TextView detailed_name = findViewById(R.id.detailed_name);
        detailed_name.setText(locationModel.getName());

        ImageView img = findViewById(R.id.detailed_imageView);
        Glide.with(img.getContext())
                .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.ic_tea).error(R.drawable.ic_tea))
                .load(locationModel.getImage())
                .into(img);

        new DetailedDescriptionFragment();
        final DetailedDescriptionFragment detailed_desc = DetailedDescriptionFragment.newInstance(locationModel);

        new DetailedHighlightFragment();
        final DetailedHighlightFragment detailed_high_light = DetailedHighlightFragment.newInstance(locationModel);

        new DetailedInfoFragment();
        final DetailedInfoFragment detailed_info = DetailedInfoFragment.newInstance(locationModel,latitude,longitude);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.DetailedLocationFragContainer,
                        detailed_desc)
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.DetailedLocationFragContainer,
                        detailed_info)
                .commit();
        getSupportFragmentManager().beginTransaction().detach(detailed_info).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.DetailedLocationNav);
        BottomNavigationView.OnNavigationItemSelectedListener DetailedLocationNav = new
                BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                getSupportFragmentManager().beginTransaction().detach(detailed_info).commit();

                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .attach(detailed_desc).commit();
                                break;
                            case R.id.location_info:
                                getSupportFragmentManager().beginTransaction().detach(detailed_desc).commit();

                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .attach(detailed_info).commit();
                                break;
                        }
                        return true;
                    }
                };
        bottomNavigationView.setOnNavigationItemSelectedListener(DetailedLocationNav);
    }
}