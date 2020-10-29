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

public class DetailedActivity extends AppCompatActivity {

    Fragment fragment = null;
    //Create new Fragment for Detailed description and Detailed info for location
    Fragment detailed_desc = new DetailedDescriptionFragment();
    Fragment detailed_info = new DetailedInfoFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        //Get intent from MainActivity
        Intent intent = getIntent();
        LocationModel locationModel = intent.getParcelableExtra("Location");
        String loc_name = locationModel.getName();
        TextView detailed_name = findViewById(R.id.detailed_name);
        detailed_name.setText(loc_name);
        //Set bundle, and put Parcel into bundle
        Bundle location_bundle = new Bundle();
        location_bundle.putParcelable("Location",intent);
        //Set desc and info
        detailed_desc.setArguments(location_bundle);
        detailed_info.setArguments(location_bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.DetailedLocationFragContainer, detailed_desc)
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
                            fragment = detailed_desc;
                            break;
                        case R.id.location_info:
                            fragment = detailed_info;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.DetailedLocationFragContainer, fragment).commit();
                    return true;
                }
            };
}