package com.example.feander.DetailedActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.feander.DetailedActivity.DetailedFragments.DetailedDescriptionFragment;
import com.example.feander.DetailedActivity.DetailedFragments.DetailedInfoFragment;
import com.example.feander.Location.LocationModel;
import com.example.feander.R;
import com.example.feander.ui.MainActivityFragment.LocationFragment;
import com.example.feander.ui.MainActivityFragment.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.GeoPoint;

public class DetailedActivity extends AppCompatActivity {

    Fragment fragment = null;
    //Create new Fragment for Detailed description and Detailed info for location
    DetailedDescriptionFragment detailed_desc = new DetailedDescriptionFragment();
    DetailedInfoFragment detailed_info = new DetailedInfoFragment();
    String loc_name;
    String loc_desc;
    String loc_location;
    GeoPoint loc_latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        //Get intent from MainActivity
        Intent intent = getIntent();
        LocationModel locationModel = intent.getParcelableExtra("Location");
        loc_name = locationModel.getName();
        loc_desc = locationModel.getDesc();
        loc_location = locationModel.getLocation();
        loc_latLng = locationModel.getLatLng();
        TextView detailed_name = findViewById(R.id.detailed_name);
        detailed_name.setText(loc_name);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.DetailedLocationFragContainer,
                        detailed_desc.newInstance(loc_name))
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.DetailedLocationNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(DetailedLocationNav);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener DetailedLocationNav = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.location_desc:
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.DetailedLocationFragContainer,
                                            detailed_desc.newInstance(loc_desc))
                                    .commit();
                            break;
                        case R.id.location_info:
                            fragment = detailed_info;
                            break;
                    }
                    return true;
                }
            };
}