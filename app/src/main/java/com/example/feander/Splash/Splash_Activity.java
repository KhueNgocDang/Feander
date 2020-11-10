package com.example.feander.Splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feander.MainActivity;
import com.example.feander.R;
import com.example.feander.SignInAndUp.ui.login.LoginActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Splash_Activity extends AppCompatActivity {
    final int SPLASH_TIME = 2000;
    final String fileName = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_);
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                String user = checkUserLoggin();
                if (user != null) {
                    Intent intent = new Intent(Splash_Activity.this, MainActivity.class);
                    intent.putExtra("userName", user);
                    Toast.makeText(getApplicationContext(), "Xin chao " + user, Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Splash_Activity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }, SPLASH_TIME);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String checkUserLoggin() {
        String line = null, contents = null;
        try {
            FileInputStream fis = getApplicationContext().openFileInput(fileName);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Loi doc thong tin" + e.getMessage(), Toast.LENGTH_LONG).show();
                // Error occurred when opening raw file for reading.
            } finally {
                contents = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
//            File file = new File(getApplicationContext().getFilesDir(), fileName);
//            Toast.makeText(this, "Da tao file", Toast.LENGTH_LONG).show();
        }
        return  contents;
    }

}
