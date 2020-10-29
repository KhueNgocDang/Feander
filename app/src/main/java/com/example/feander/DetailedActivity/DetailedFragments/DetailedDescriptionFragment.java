package com.example.feander.DetailedActivity.DetailedFragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.feander.R;

public class DetailedDescriptionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed_description, container, false);
        String desc = getArguments().getString("Location");
        TextView detailed_desc = view.findViewById(R.id.detailed_desc);
        detailed_desc.setText(desc);
        return view;
    }
}