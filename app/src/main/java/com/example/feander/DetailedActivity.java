package com.example.feander;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        final Intent intent = getIntent();
        String loc_name = intent.getStringExtra("name");

        TextView name = findViewById(R.id.name);

        Button button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_2 = new Intent(DetailedActivity.this, MapsActivity2.class);

                intent_2.putExtra("latitude",intent.getDoubleExtra("latitude",0));
                intent_2.putExtra("longitude",intent.getDoubleExtra("longitude",0));
                startActivity(intent_2);
            }
        });

        name.setText(loc_name);
    }
}