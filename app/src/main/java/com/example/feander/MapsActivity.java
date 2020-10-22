package com.example.feander;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker marker;
    //Use location manager to manage location
    LocationManager locationManager;
    LocationListener locationListener;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference locationRef = db.collection("Location");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_PERMISSION);
        }
        else
            {
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        Geocoder geocoder = new Geocoder(getApplicationContext());
                        try {
                            List<Address> addresses =
                                    geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            String loc = addresses.get(0).getLocality()+":"+addresses.get(0).getCountryName();
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            if(marker!=null)
                            {
                                marker.remove();
                            }
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(loc));
                            mMap.setMaxZoomPreference(20);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18.0f));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
    }

    public void onClick(View v)
    {
        if (v.getId() == R.id.F_B)
        {
            EditText tf_location = findViewById(R.id.StartLocation);
            String location = tf_location.getText().toString();
            List<Address> addressList = null;
            MarkerOptions markerOptions = new MarkerOptions();

            if(!location.equals(""))
            {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 3);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for(int i = 0; i<addressList.size();i++)
                {
                    Address currAddress = addressList.get(i);
                    LatLng latLng = new LatLng(currAddress.getLatitude(),currAddress.getLongitude());
                    markerOptions.position(latLng);
                    markerOptions.title("Search result");
                    mMap.addMarker(markerOptions);

                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

        }
        mMap.setMyLocationEnabled(true);
    }
}