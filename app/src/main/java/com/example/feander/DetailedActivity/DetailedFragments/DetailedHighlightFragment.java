package com.example.feander.DetailedActivity.DetailedFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
        View view = inflater.inflate(R.layout.fragment_highlight, container, false);
        ImageView imageView1 =view.findViewById(R.id.SubImageView1);
        ImageView imageView2 =view.findViewById(R.id.SubImageView2);
        ImageView imageView3 =view.findViewById(R.id.SubImageView3);
        ImageView imageView4 =view.findViewById(R.id.SubImageView4);
        ImageView imageView5 =view.findViewById(R.id.SubImageView5);
        ImageView imageView6 =view.findViewById(R.id.SubImageView6);

        Glide.with(imageView1.getContext())
                .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.ic_tea).error(R.drawable.ic_tea))
                .load(mParam1.getSub_image1())
                .into(imageView1);

        Glide.with(imageView2.getContext())
                .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.ic_tea).error(R.drawable.ic_tea))
                .load(mParam1.getSub_image2())
                .into(imageView2);

        Glide.with(imageView3.getContext())
                .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.ic_tea).error(R.drawable.ic_tea))
                .load(mParam1.getSub_image3())
                .into(imageView3);

        Glide.with(imageView4.getContext())
                .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.ic_tea).error(R.drawable.ic_tea))
                .load(mParam1.getSub_image4())
                .into(imageView4);

        Glide.with(imageView5.getContext())
                .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.ic_tea).error(R.drawable.ic_tea))
                .load(mParam1.getSub_image5())
                .into(imageView5);

        Glide.with(imageView6.getContext())
                .applyDefaultRequestOptions(RequestOptions.placeholderOf(R.drawable.ic_tea).error(R.drawable.ic_tea))
                .load(mParam1.getSub_image6())
                .into(imageView6);

        TextView location_text = view.findViewById(R.id.location_text1);
        location_text.setText("Ảnh cho địa điểm");

        TextView location_text1 = view.findViewById(R.id.location_text2);
        if(mParam1.isSeller().equals("true")){location_text1.setText("Ảnh sản phẩm:");}
        else {location_text1.setText("");}

        return view;
    }
}