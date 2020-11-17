package com.example.feander.ui.MainActivityFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.feander.R;
import com.example.feander.User.data.DataSource;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link User_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class User_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARGS_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String userName, phoneNumber;

    public User_Fragment(String userName) {
        // Required empty public constructor
        this.userName = userName;
    }

    public User_Fragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment User_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static User_Fragment newInstance(String param1, String param2) {
        User_Fragment fragment = new User_Fragment();
        Bundle args = new Bundle();
        args.putString(ARGS_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARGS_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        TextView userNameView = view.findViewById(R.id.username);
        userNameView.setText(userName);
        final TextView phoneNumberView = view.findViewById(R.id.phone_number);
        new DataSource().getUserPhoneNumber(userName).observeForever(new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                   phoneNumberView.setText(s);
                }
                if(s.equals("")){
                    phoneNumberView.setText("Chưa có thông tin");
                }
            }
        });
        return view;
    }
}