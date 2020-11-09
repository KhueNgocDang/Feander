package com.example.feander.ui.MainActivityFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feander.DetailedActivity.DetailedActivity;
import com.example.feander.Location.LocationAdapter;
import com.example.feander.Location.LocationModel;
import com.example.feander.R;
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

public class LocationFragment extends Fragment implements LocationAdapter.OnLocationListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private LocationAdapter adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public List<LocationModel> locationList;
    public static LocationFragment fragment;
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
        assert bundle != null;
        current_location = new LatLng(bundle.getDouble(ARG_PARAM1),bundle.getDouble(ARG_PARAM2));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.FirestoreList);

        db.collection("Location").get()
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
}