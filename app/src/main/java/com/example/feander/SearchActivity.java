package com.example.feander;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feander.DetailedActivity.DetailedActivity;
import com.example.feander.Location.LocationAdapter;
import com.example.feander.Location.LocationModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class SearchActivity extends AppCompatActivity implements LocationAdapter.OnLocationListener{

    private RecyclerView recyclerView;
    private LocationAdapter adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Location location;
    public List<LocationModel> locationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final Intent get_intent = getIntent();
        final LatLng latLng = get_intent.getParcelableExtra("current_location");
        final String Type = get_intent.getStringExtra("type");

        Toolbar toolbar = findViewById(R.id.SearchToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Tìm kiếm");

        recyclerView = findViewById(R.id.recyclerView);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

         db.collection("Location").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            LocationModel locationModel = queryDocumentSnapshot.toObject(LocationModel.class);
                            locationModel.setDistance(latLng);
                            if(Type.equals("tearoom")&&locationModel.isTea_room().equals("true"))
                                {locationList.add(locationModel);}
                            if(Type.equals("retailer")&&locationModel.isSeller().equals("true"))
                            {locationList.add(locationModel);}
                            if(Type.equals("both")) {locationList.add(locationModel);}
                        }
                        Collections.sort(locationList, new Comparator<LocationModel>() {
                            @Override
                            public int compare(LocationModel o1, LocationModel o2) {
                                if(o1.getDistance()<o2.getDistance())
                                    return -1;
                                else if(o2.getDistance()<o1.getDistance())
                                    return 1;
                                return 0;
                            }
                        });
                        recyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","onFailure" +e.getMessage());
                    }
                });
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new LocationAdapter(this, locationList, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_activity_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onLocationClick(int position) {
        locationList.get(position);
        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra("Location",locationList.get(position));
        startActivity(intent);
    }

    private void byDistance(List<LocationModel> LocationList, final int Type){
        Collections.sort(LocationList, new Comparator<LocationModel>() {
            @Override
            public int compare(LocationModel o1, LocationModel o2) {
                if (Type == 1) {
                    if(o1.getDistance()<o2.getDistance())
                        return -1;
                    else if(o2.getDistance()<o1.getDistance())
                        return 1;
                    return 0;
                }
                else {
                    if(o1.getDistance()<o2.getDistance())
                        return -1;
                    else if(o2.getDistance()<o1.getDistance())
                        return 1;
                        return 0;}
            }
        });
    }
}