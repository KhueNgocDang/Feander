package com.example.feander.DetailedActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.feander.DetailedActivity.DetailedFragments.DetailedDescriptionFragment;
import com.example.feander.DetailedActivity.DetailedFragments.DetailedHighlightFragment;
import com.example.feander.DetailedActivity.DetailedFragments.DetailedInfoFragment;
import com.example.feander.Location.LocationModel;
import com.example.feander.R;
import com.example.feander.User.data.DataSource;
import com.example.feander.User.data.Result;
import com.example.feander.User.data.model.LoggedInUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

public class DetailedActivity extends AppCompatActivity {

    Location location;
    DetailedDescriptionFragment detailed_desc;
    DetailedHighlightFragment detailed_high_light;
    DetailedInfoFragment detailed_info;
    FloatingActionButton saveButton;
    String locationId, userId, idDocument;
    boolean isSaved = false;
    MutableLiveData<Boolean> isSave = new MutableLiveData<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        saveButton = findViewById(R.id.save_location_button);
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

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }

        //Get intent from MainActivity
        Intent intent = getIntent();
        final LocationModel locationModel = intent.getParcelableExtra("Location");

        detailed_desc = DetailedDescriptionFragment.newInstance(locationModel);
        detailed_high_light = DetailedHighlightFragment.newInstance(locationModel);
        detailed_info = DetailedInfoFragment.newInstance(locationModel, latitude, longitude);
        locationId = intent.getStringExtra("locationId");
        userId = intent.getStringExtra("userId");
        isSave.setValue(false);
        checkSave(userId, locationId);
        final ViewPager viewPager = findViewById(R.id.DetailedViewpager);
        setupViewPager(viewPager);

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                // How far the user has to scroll before it locks the parent vertical scrolling.
                final int margin = 10;
                final int fragmentOffset = v.getScrollX() % v.getWidth();

                if (fragmentOffset > margin && fragmentOffset < v.getWidth() - margin) {
                    viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        Toolbar toolbar = findViewById(R.id.DetailedToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(locationModel.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView img = findViewById(R.id.DetailedHeader);
        Glide.with(img.getContext())
                .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.ic_tea).error(R.drawable.ic_tea))
                .load(locationModel.getImage())
                .into(img);

        TabLayout tabLayout = findViewById(R.id.DetailedTabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        // TODO
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        traceSaved();
    }

    private void setupViewPager(ViewPager viewPager) {
        DetailedPagerAdapter adapter = new DetailedPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFrag(detailed_desc, "Info");
        adapter.addFrag(detailed_high_light, "Highlight");
        adapter.addFrag(detailed_info, "On Map");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveLocation(View view) {
        if (userId != null) {
            if(!isSave.getValue())
            new DataSource().writeData("users" + "/" + getIntent().getStringExtra("userId") + "/" + "savedLocation"
                    , new String[]{"locationId"}, new String[]{locationId}).observe(this, new Observer<Result>() {
                @Override
                public void onChanged(Result result) {
                    if (result instanceof Result.Success) {
                        showResult("Lưu thành công");
                        isSave.setValue(true);
                    } else if (result instanceof Result.Error) {
                        showResult("Lưu thất bại");
                    }
                }
            });
            else new DataSource().deleteDocument("users" + "/" + getIntent().getStringExtra("userId") + "/" + "savedLocation", idDocument).observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean){
                        showResult("Bỏ lưu thành công");
                        isSave.setValue(false);
                    }else {
                        showResult("Bỏ lưu thất bại");
                    }
                }
            });
        } else showResult("Bạn đang xem với tư cách khách");
    }


    private void checkSave(String userId, String locationId) {
        if (userId != null) {
            new DataSource().findData("users" + "/" + getIntent().getStringExtra("userId") + "/" + "savedLocation", "locationId", new String[]{locationId}).observe(this,
                    new Observer<QuerySnapshot>() {
                        @Override
                        public void onChanged(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots != null) {
                                if (queryDocumentSnapshots.size() > 0) {
                                    isSave.setValue(true);
                                    idDocument = queryDocumentSnapshots.getDocuments().get(0).getId();
                                } else {
                                    isSave.setValue(false);
                                }
                            }
                        }
                    });
        }
    }

    private void showResult(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    private void traceSaved() {
        isSave.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    saveButton.setImageResource(R.drawable.tick_icon);
                } else {
                    saveButton.setImageResource(R.drawable.save_icon);
                }
            }
        });
    }
}