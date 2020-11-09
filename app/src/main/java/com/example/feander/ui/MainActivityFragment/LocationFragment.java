package com.example.feander.ui.MainActivityFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feander.DetailedActivity.DetailedActivity;
import com.example.feander.DetailedActivity.DetailedFragments.DetailedDescriptionFragment;
import com.example.feander.Location.LocationAdapter;
import com.example.feander.Location.LocationModel;
import com.example.feander.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class LocationFragment extends Fragment implements LocationAdapter.OnLocationListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private LocationAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public List<LocationModel> locationList;
    public static LocationFragment fragment;
    TextView curr_location;
    String tets = "test";
    private LatLng current_location;

    public LocationFragment() {
        // Required empty public constructor
    }

    public static LocationFragment newInstance(double latitude, double longitude) {
        if(fragment==null){
            fragment = new LocationFragment();
        }
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, latitude);
        args.putDouble(ARG_PARAM2, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        locationList = new ArrayList<>();
        current_location = new LatLng(bundle.getDouble(ARG_PARAM1),bundle.getDouble(ARG_PARAM2));
        tets = Double.toString(bundle.getDouble(ARG_PARAM1)) +"," + Double.toString(bundle.getDouble(ARG_PARAM2));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        curr_location = view.findViewById(R.id.curr_location);
        curr_location.setText(tets);
        recyclerView = view.findViewById(R.id.FirestoreList);

        Task<QuerySnapshot> querySnapshotTask = db.collection("Location").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            LocationModel locationModel = queryDocumentSnapshot.toObject(LocationModel.class);
                            locationModel.setDistance(current_location);
                            locationList.add(locationModel);
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
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());

                        recyclerView.setLayoutManager(linearLayoutManager);

                        recyclerView.setAdapter(adapter);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","onFailure" +e.getMessage());
                    }
                });
        adapter = new LocationAdapter(getContext(), locationList, this);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onLocationClick(int position) {
        locationList.get(position);
        Intent intent = new Intent(getContext(), DetailedActivity.class);
        intent.putExtra("Location",locationList.get(position));
        startActivity(intent);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}