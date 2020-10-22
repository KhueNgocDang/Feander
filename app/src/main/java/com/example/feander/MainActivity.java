package com.example.feander;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.example.feander.ui.MapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity  {

    Fragment fragment = null;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference locationRef = db.collection("Location");

    public List<LocationModel> locationModelList = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new ListFragment(), ListFragment.class.getSimpleName())
                .commit();

        bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.list_location:
                            fragment = new ListFragment();
                            break;
                        case R.id.map_location:
                            fragment = new MapFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    return true;
                }
            };
    @Override
    public void onStart() {
        super.onStart();

        db.collection("Location").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (DocumentSnapshot documentSnapshot: task.getResult()){
                                locationModelList.add(documentSnapshot.toObject(LocationModel.class));
                            }
                            Log.d("TAG", locationModelList.toString());
                        }else
                            Log.d("TAG","Error getting documents:", task.getException());
                    }
                });

    }
}