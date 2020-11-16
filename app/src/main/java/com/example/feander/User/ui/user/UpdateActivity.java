package com.example.feander.User.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.feander.R;

public class UpdateActivity extends AppCompatActivity {
    private String userName;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        userName = getIntent().getStringExtra("userName");
        EditText userNameEditText = findViewById(R.id.username);
        userNameEditText.setText(userName);
        Button updateButton = findViewById(R.id.update_button);
        Button changePasswodButton = findViewById(R.id.change_password_button);
    }

    public void updateUser(View view) {

    }


    public void changePassword(View view) {
    }


}