package com.example.feander.DetailedActivity.DetailedFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feander.Location.LocationModel;
import com.example.feander.R;

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
        TextView desc = view.findViewById(R.id.description);
        TextView webs = view.findViewById(R.id.website);
        TextView add = view.findViewById(R.id.address);
        TextView dis = view.findViewById(R.id.distance);
        desc.setText("Mô tả: "+mParam1.getDesc());
        dis.setText("Khoảng cách:"+mParam1.getDistance()+"m");
        add.setText('\n'+"Địa chỉ:"+mParam1.getLocation() +'\n');
        webs.setText(mParam1.getWebsite());
        webs.setMovementMethod(LinkMovementMethod.getInstance());
        webs.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                Intent browser= new Intent(Intent.ACTION_VIEW, Uri.parse(mParam1.getWebsite()));
                startActivity(browser);
            }

        });


        return view;
    }
}