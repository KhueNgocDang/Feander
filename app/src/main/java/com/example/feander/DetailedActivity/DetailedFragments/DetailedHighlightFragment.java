package com.example.feander.DetailedActivity.DetailedFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.feander.Location.LocationModel;
import com.example.feander.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedHighlightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedHighlightFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static DetailedHighlightFragment fragment;

    // TODO: Rename and change types of parameters
    private LocationModel mParam1;

    public DetailedHighlightFragment() {
        // Required empty public constructor
    }

    public static DetailedHighlightFragment newInstance(LocationModel locationModel) {
        if(fragment==null){
            fragment = new DetailedHighlightFragment();
        }
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, locationModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mParam1 = bundle.getParcelable(ARG_PARAM1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_highlight, container, false);
    }
}