package com.example.feander.SignInAndUp.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.feander.SignInAndUp.data.model.LoggedInUser;
import com.example.feander.SignInAndUp.data.model.UncorrectPassWordException;
import com.example.feander.SignInAndUp.ui.login.LoginActivity;
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

//import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    FirebaseFirestore dataSource = FirebaseFirestore.getInstance();
    private MutableLiveData<Result> resultLive = new MutableLiveData<>();
    private Result loginResult = new Result.WaitingCheckUser();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MutableLiveData<Result> login(String username, String password) {
        try {
//            if (checkUsers(username, password)) {
//                LoggedInUser loggedInUser =
//                        new LoggedInUser(
//                                java.util.UUID.randomUUID().toString(),
//                                username);
//                return new Result.Success<LoggedInUser>(loggedInUser);
//            } else {
//                return new Result.Error(new UncorrectPassWordException());
//            }
            return checkUsers(username, password);

        } catch (Exception e) {
            resultLive.setValue(new Result.Error(new IOException("Error logging in", e)));
            return resultLive;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logout() {
    }
    private MutableLiveData<Result> checkUsers(final String userName, final String passWord) {
//        dataSource.collection("users").whereEqualTo("userNames", userName)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if ((task.isSuccessful()) && (task.getResult().size() > 0)) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("PassWord", document.getId() + " => " + document.getData());
//                                if (document.get("passWord").equals(passWord)) {
//                                    loginSuccess = true;
//                                } else loginSuccess = false;
//                            }
//                        } else {
//                            Log.w("PassWord", "Error getting documents.", task.getException());
//                            loginSuccess = false;
//                        }
//                    }
//                });
//        return loginSuccess;
        dataSource.collection("users").whereEqualTo("userNames", userName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if ((task.isSuccessful()) && (task.getResult().size() > 0)) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("PassWord", document.getId() + " => " + document.getData());
                                if (document.get("passWord").equals(passWord)) {
                                    LoggedInUser loggedInUser =
                                            new LoggedInUser(
                                                    java.util.UUID.randomUUID().toString(),
                                                    userName);
//                                    loginResult = new Result.Success<LoggedInUser>(loggedInUser);
                                    resultLive.setValue(new Result.Success<LoggedInUser>(loggedInUser));
                                } else {
//                                    loginResult = new Result.Error(new UncorrectPassWordException());
                                    resultLive.setValue(new Result.Error(new UncorrectPassWordException()));
                                }
                            }
                        } else {
                            Log.w("PassWord", "Error getting documents.", task.getException());
//                            loginResult = new Result.Error(new Exception("Error logging in", task.getException()));
                            resultLive.setValue(loginResult);
                        }
                    }
                });
        return resultLive ;
    }

}