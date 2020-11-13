package com.example.feander.ui.MainActivityFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.motion.widget.DesignTool;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.feander.R;
import com.example.feander.SearchActivity;

public class SearchFragment extends Fragment {

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        final Intent intent = new Intent(getContext(), SearchActivity.class);
        SearchView searchView = view.findViewById(R.id.SearchViewSearchFrag);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        return view;
    }
}