package com.example.feander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private RecyclerView storelist;
    private Button BtnMove;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseFirestore = FirebaseFirestore.getInstance();
        storelist = findViewById(R.id.FirestoreList);

        //Button for moving to map activity
        BtnMove = findViewById(R.id.D_Button);
        BtnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToMapActivity();
            }
        });

        //Query from firebase firestore
        Query query = firebaseFirestore.collection("Location");
        //Recycler option
        FirestoreRecyclerOptions<LocationModel> options = new FirestoreRecyclerOptions.
                Builder<LocationModel>()
                .setQuery(query, LocationModel.class).build();
        adapter = new FirestoreRecyclerAdapter<LocationModel, LocationViewHolder>(options) {
            @NonNull
            @Override
            public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_location_single, parent, false);
                return new LocationViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull LocationViewHolder holder, int position, @NonNull LocationModel model) {
                holder.list_name.setText(model.getName());
                holder.list_latlng.setText(model.getLatLng() + "");
                holder.list_type.setText(model.isTeaShop()+"");
            }
        };
        storelist.setHasFixedSize(true);
        storelist.setLayoutManager(new LinearLayoutManager(this));
        storelist.setAdapter(adapter);
    }

    private class LocationViewHolder extends RecyclerView.ViewHolder {

        private TextView list_name;
        private TextView list_type;
        private TextView list_latlng;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            list_name = itemView.findViewById(R.id.list_name);
            list_type = itemView.findViewById(R.id.list_type);
            list_latlng = itemView.findViewById(R.id.list_latlng);

        }

    }

    private void moveToMapActivity(){

        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}