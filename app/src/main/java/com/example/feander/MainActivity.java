package com.example.feander;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feander.ui.MainActivityFragment.LocationFragment;
import com.example.feander.ui.MainActivityFragment.MapFragment;
import com.example.feander.ui.MainActivityFragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        final LocationFragment locationFragment = new LocationFragment();
        final MapFragment mapFragment = new MapFragment();
        final SearchFragment searchFragment = new SearchFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, locationFragment)
                .commit();
        getSupportFragmentManager().beginTransaction().detach(locationFragment).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mapFragment)
                .commit();
        getSupportFragmentManager().beginTransaction().detach(mapFragment).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,searchFragment)
                .commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);
        BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new
                BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.location_info:
                                getSupportFragmentManager().beginTransaction().detach(mapFragment).commit();
                                getSupportFragmentManager().beginTransaction().detach(searchFragment).commit();
                                getSupportFragmentManager().beginTransaction().attach(locationFragment).commit();
                                break;
                            case R.id.map_location:
                                getSupportFragmentManager().beginTransaction().detach(locationFragment).commit();
                                getSupportFragmentManager().beginTransaction().detach(searchFragment).commit();
                                getSupportFragmentManager().beginTransaction().attach(mapFragment).commit();
                                break;
                            case R.id.location_search:
                                getSupportFragmentManager().beginTransaction().detach(locationFragment).commit();
                                getSupportFragmentManager().beginTransaction().detach(mapFragment).commit();
                                getSupportFragmentManager().beginTransaction().attach(searchFragment).commit();
                        }
                        return true;
                    }
                };
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
    }

}