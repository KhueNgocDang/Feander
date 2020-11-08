package com.example.feander.DetailedActivity.DetailedFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feander.Location.LocationModel;
import com.example.feander.R;
import com.example.feander.ui.MainActivityFragment.LocationFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailedDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailedDescriptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    public static DetailedDescriptionFragment fragment;

    // TODO: Rename and change types of parameters
    private LocationModel mParam1;

    public DetailedDescriptionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetailedDescriptionFragment newInstance(LocationModel locationModel) {
        if(fragment==null){
            fragment = new DetailedDescriptionFragment();
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
        View view = inflater.inflate(R.layout.fragment_detailed_description, container, false);
        TextView desc = view.findViewById(R.id.textView);
        desc.setText("Mô tả:"+mParam1.getDesc()+'\n'+"Địa chỉ:"+mParam1.getLocation());

        return view;
    }
}