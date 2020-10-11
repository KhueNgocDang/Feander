package com.example.feander;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity  {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference locationRef = db.collection("Location");

    private LocationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = locationRef.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<LocationModel> options = new FirestoreRecyclerOptions.Builder<LocationModel>()
                .setQuery(query, LocationModel.class)
                .build();

        adapter = new LocationAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.FirestoreList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

//        adapter.setOnItemClickListener(new LocationAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                LocationModel location = documentSnapshot.toObject(LocationModel.class);
//                Double longitude = Double.parseDouble(location.getLongitude()) ;
//                Double latitude = Double.parseDouble(location.getLatitude());
//
//                //LatLng latLng = location.getLatLng();
//
//                Intent intent = new Intent(MainActivity.this, MapsActivity2.class);
//                intent.putExtra("latitude",latitude);
//                intent.putExtra("longitude",longitude);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}