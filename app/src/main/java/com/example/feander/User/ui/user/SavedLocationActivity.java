package com.example.feander.User.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.feander.R;

public class SavedLocationActivity extends AppCompatActivity {
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location);
        userId = getIntent().getStringExtra("userId");
    }

    private void getAllSavedLocation(String userId) {

    }
}