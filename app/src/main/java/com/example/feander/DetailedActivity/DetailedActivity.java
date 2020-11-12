package com.example.feander.DetailedActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.TextView;

import com.example.feander.DetailedActivity.DetailedFragments.DetailedDescriptionFragment;
import com.example.feander.DetailedActivity.DetailedFragments.DetailedInfoFragment;
import com.example.feander.Location.LocationModel;
import com.example.feander.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        //Get intent from MainActivity
        Intent intent = getIntent();
        final LocationModel locationModel = intent.getParcelableExtra("Location");
        final LatLng current_location = intent.getParcelableExtra("current_location");

        TextView detailed_name = findViewById(R.id.detailed_name);
        detailed_name.setText(locationModel.getName());

        new DetailedDescriptionFragment();
        final DetailedDescriptionFragment detailed_desc =
                DetailedDescriptionFragment.newInstance(locationModel);
        new DetailedInfoFragment();
        final DetailedInfoFragment detailed_info =
                DetailedInfoFragment.newInstance
                        (locationModel,current_location.latitude,current_location.longitude);

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