package com.example.feander.ui.MainActivityFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LocationFragment extends Fragment implements LocationAdapter.OnLocationListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static LocationFragment fragment;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<LocationModel> locationList = new ArrayList<>();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private LatLng current_location;
    View view;

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
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        assert bundle != null;
        current_location = new LatLng(bundle.getDouble(ARG_PARAM1),bundle.getDouble(ARG_PARAM2));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_list, container, false);
            recyclerView = view.findViewById(R.id.FirestoreList);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

            Task<QuerySnapshot> querySnapshotTask = db.collection("Location").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                LocationModel locationModel = queryDocumentSnapshot.toObject(LocationModel.class);
                                locationModel.setDistance(current_location);
                                locationList.add(locationModel);
                                Log.d("TAG", "onSuccess" + locationModel.getName());
                            }
                            Collections.sort(locationList, new Comparator<LocationModel>() {
                                @Override
                                public int compare(LocationModel o1, LocationModel o2) {
                                    if (o1.getDistance() < o2.getDistance())
                                        return -1;
                                    else if (o2.getDistance() < o1.getDistance())
                                        return 1;
                                    return 0;
                                }
                            });
                            recyclerView.setAdapter(adapter);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG", "onFailure" + e.getMessage());
                        }
                    });
            recyclerView.setLayoutManager(linearLayoutManager);

            adapter = new LocationAdapter(getContext(), locationList, this);
        }
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);

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
        super.onCreateOptionsMenu(menu, inflater);
    }
}
