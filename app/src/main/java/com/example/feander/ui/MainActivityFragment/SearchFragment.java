package com.example.feander.ui.MainActivityFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

        ImageView banner = view.findViewById(R.id.search_frag_banner);
        Glide.with(banner.getContext())
                .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.app_background).error(R.drawable.ic_tea))
                .load("https://firebasestorage.googleapis.com/v0/b/feander-91f8f.appspot.com/o/Y%C3%AAu%20tr%C3%A0%20vi%E1%BB%87t_Banner.jpg?alt=media&token=74341e28-93d3-4791-bd93-1bf945715a6c")
                .into(banner);

        Button tearoom_filter = view.findViewById(R.id.tearoom);
        tearoom_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("current_location",current_location);
                intent.putExtra("type","tearoom");
                startActivity(intent);
            }
        });
        Button retailer_filter = view.findViewById(R.id.retailer);
        retailer_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("current_location",current_location);
                intent.putExtra("type","retailer");
                startActivity(intent);
            }
        });
        Button opening_filter = view.findViewById(R.id.opening);
        opening_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("current_location",current_location);
                intent.putExtra("type","opening");
                startActivity(intent);
            }
        });

        SearchView searchView = view.findViewById(R.id.SearchViewSearchFrag);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("current_location",current_location);
                intent.putExtra("type","both");
                startActivity(intent);
            }
        });

        return view;
    }
}