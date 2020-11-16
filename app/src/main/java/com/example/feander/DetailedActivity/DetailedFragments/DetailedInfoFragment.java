package com.example.feander.DetailedActivity.DetailedFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.feander.Location.LocationModel;
import com.example.feander.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class DetailedInfoFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    public static DetailedInfoFragment fragment;

    private LocationModel mParam1;
    LatLng latLng;
    LatLng current_position;
    GoogleMap mMap;
    Marker location_marker;
    Marker user_marker;

    public DetailedInfoFragment() {
        // Required empty public constructor
    }

    public static DetailedInfoFragment newInstance(LocationModel locationModel,double latitude, double longitude) {
        if (fragment == null) {
            fragment = new DetailedInfoFragment();
        }
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, locationModel);
        args.putDouble(ARG_PARAM2, latitude);
        args.putDouble(ARG_PARAM3, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            List<Marker> markersList = new ArrayList<Marker>();
            latLng = new LatLng(mParam1.getLatLng().getLatitude(), mParam1.getLatLng().getLongitude());

            location_marker = googleMap.addMarker(new MarkerOptions().position(latLng)
                    .title(mParam1.getName())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            user_marker = googleMap.addMarker(new MarkerOptions().position(current_position)
                    .title("Vị trí hiện tại"));

            markersList.add(location_marker);
            markersList.add(user_marker);


            //get the latLngbuilder from the marker list
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker m : markersList) {
                builder.include(m.getPosition());
            }

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (Math.min(width, height)*0.2);

            //Create bounds here
            LatLngBounds bounds = builder.build();

            //Create camera with bounds
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width,height,padding);

            googleMap.moveCamera(cu);

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        assert bundle != null;
        current_position = new LatLng(bundle.getDouble(ARG_PARAM2),bundle.getDouble(ARG_PARAM3));
        mParam1 = bundle.getParcelable(ARG_PARAM1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}