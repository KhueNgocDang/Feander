package com.example.feander.MainActivity.MainFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.feander.R;
import com.example.feander.SearchActivity;
import com.google.android.gms.maps.model.LatLng;

public class SearchFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static SearchFragment fragment;
    private LatLng current_location;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(double latitude, double longitude) {
        if(fragment==null){
            fragment = new SearchFragment();
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
        assert bundle != null;
        current_location = new LatLng(bundle.getDouble(ARG_PARAM1),bundle.getDouble(ARG_PARAM2));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
/*
        Intent intent = new Intent(getContext(), SearchActivity.class);
        intent.putExtra("current_location",current_location);*/

        SearchView searchView = view.findViewById(R.id.SearchViewSearchFrag);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("current_location",current_location);
                startActivity(intent);
            }
        });

        return view;
    }
}