package com.example.feander.SignInAndUp.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.feander.SignInAndUp.data.model.LoggedInUser;
import com.example.feander.SignInAndUp.ui.login.LoginActivity;
import com.example.feander.SignInAndUp.ui.login.LoginViewModel;

import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;
    final String fileName = "user";
    private LoginViewModel loginViewModel;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MutableLiveData<Result> login(String username, String password) {
        // handle login
//        Result<LoggedInUser> result = dataSource.login(username, password);
//        if (result instanceof Result.Success) {
//            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
//        }
//        return result;
        final MutableLiveData<Result> resultLive = dataSource.login(username, password);
        resultLive.observeForever(new Observer<Result>() {
            @Override
            public void onChanged(Result result) {
                if (result instanceof Result.Success) {
                    LoggedInUser loggedInUser = ((Result.Success<LoggedInUser>) result).getData();
                    setLoggedInUser(loggedInUser);
                    storeLoggedInUser(loggedInUser.getDisplayName());
                    resultLive.removeObserver(this);
                } else if (result instanceof Result.Error) {
                    resultLive.removeObserver(this);
                }
            }
        });

        //        if (resultLive.getValue() instanceof Result.Success) {
//            setLoggedInUser(((Result.Success<LoggedInUser>) resultLive.getValue()).getData());
//        }
        return resultLive;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void storeLoggedInUser(String fileContents) {
        try (@SuppressLint("RestrictedApi") FileOutputStream fos = loginViewModel.getCallingActivity().openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (Exception e) {
            Log.d("loggedinuser", "that bai");
        }
    }
    public void setLoginViewModel(LoginViewModel loginViewModel) {
        this.loginViewModel = loginViewModel;
    }
}