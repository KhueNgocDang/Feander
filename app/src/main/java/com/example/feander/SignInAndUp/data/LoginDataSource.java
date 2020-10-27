package com.example.feander.SignInAndUp.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.feander.SignInAndUp.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    FirebaseFirestore dataSource = FirebaseFirestore.getInstance();
    boolean loginSuccess = false;
    final String fileName = "user";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Result<LoggedInUser> login(String username, String password) {
        try {
            if (checkUsers(username, password)) {
                LoggedInUser loggedInUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                username);
                storeLoggedInUser(username);
                return new Result.Success<LoggedInUser>(loggedInUser);
            } else {
                return new Result.Error();
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logout() {
        storeLoggedInUser("");
    }
    private boolean checkUsers(String userName, final String passWord) {
        dataSource.collection("users").whereEqualTo("userNames", userName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if ((task.isSuccessful()) && (task.getResult().size() > 0)) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("PassWord", document.getId() + " => " + document.getData());
                                if (document.get("passWord").equals(passWord)) {
                                    loginSuccess = true;
                                } else loginSuccess = false;
                            }
                        } else {
                            Log.w("PassWord", "Error getting documents.", task.getException());
                            loginSuccess = false;
                        }
                    }
                });
        return loginSuccess;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void storeLoggedInUser(String fileContents) {
        try (@SuppressLint("RestrictedApi") FileOutputStream fos = getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (Exception e) {
            Log.e("Luu thong tin", "that bai");
        }

    }
}