package com.example.feander.User.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.feander.DetailedActivity.DetailedActivity;
import com.example.feander.Location.LocationAdapter;
import com.example.feander.Location.LocationModel;
import com.example.feander.R;
import com.example.feander.User.data.DataSource;

import java.util.ArrayList;

public class SavedLocationActivity extends AppCompatActivity implements LocationAdapter.OnLocationListener {
    private String userId;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    LocationAdapter adapter;
    ArrayList<LocationModel> locationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location);
        userId = getIntent().getStringExtra("userId");
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAllSavedLocation(userId);
    }

    private void getAllSavedLocation(String userId) {
        new DataSource().getSavedLocation(userId).observe(this, new Observer<ArrayList<LocationModel>>() {
            @Override
            public void onChanged(ArrayList<LocationModel> locationModels) {
                if (locationModels.size() > 0) {
                    locationList = locationModels;
                    adapter = new LocationAdapter(getThisClass(), locationList, getThisClass());
                    adapter.setCallingActivity(getThisClass());
                    recyclerView.setAdapter(adapter);
                    Log.d("list loaction", locationModels.toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Chưa lưu cửa hàng nào", Toast.LENGTH_LONG).show();
                    finish();
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }
    private SavedLocationActivity getThisClass(){
        return this;
    }


    @Override
    public void onLocationClick(int position) {
        locationList.get(position);
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra("Location", locationList.get(position));
        intent.putExtra("userId", userId);
        intent.putExtra("locationId", locationList.get(position).getLocationId());
        startActivity(intent);
    }
}