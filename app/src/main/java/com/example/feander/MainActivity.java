package com.example.feander;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.feander.User.data.Repository;
import com.example.feander.User.ui.user.LoginActivity;
import com.example.feander.User.ui.user.SavedLocationActivity;
import com.example.feander.User.ui.user.UpdateActivity;
import com.example.feander.ui.MainActivityFragment.LocationFragment;
import com.example.feander.ui.MainActivityFragment.SearchFragment;
import com.example.feander.ui.MainActivityFragment.User_Fragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    LatLng latLng;
    Location location;
    private String userName, userId;
    private final List<Fragment> mFragmentList  = new ArrayList<>();
    LocationFragment locationFragment;
    SearchFragment searchFragment;
    User_Fragment user_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        double latitude = 21.028511;
        double longitude = 105.804817;
        boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        userName = getIntent().getStringExtra("userName");
        userId = getIntent().getStringExtra("id");
//        Log.d("userId", userId);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
        
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

        locationFragment = LocationFragment.newInstance(latitude, longitude);
        locationFragment.setUserId(userId);
        locationFragment.setCallingActivty(this);
        searchFragment = SearchFragment.newInstance(latitude, longitude);
        user_fragment = new User_Fragment(userName);

        final ViewPager viewPager = findViewById(R.id.fragment_container);
        setupViewPager(viewPager);

        final Intent map_intent = new Intent(this, MapsActivity.class);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);
        BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new
                BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.location_info:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_search:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.user:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.map_location:
                                startActivity(map_intent);
                                break;
                        }
                        return true;
                    }
                };
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.action_search).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.location_info).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.user).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        if (android.os.Build.VERSION.SDK_INT < 28 )
            finish();
        else onDestroy();
    }

    public void showSavedLocation(View view) {
        if (userName != null)
            startActivity(new Intent(this, SavedLocationActivity.class).putExtra("userId", userId));
        else
            Toast.makeText(this, "Bạn đang xem với tư cách khách", Toast.LENGTH_LONG).show();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFrag(searchFragment, "Tìm kiếm");
        adapter.addFrag(locationFragment, "Danh sách");
        adapter.addFrag(user_fragment , "Người dùng");
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
    }
}