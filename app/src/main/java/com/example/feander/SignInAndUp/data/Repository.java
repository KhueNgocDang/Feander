package com.example.feander.SignInAndUp.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.feander.SignInAndUp.data.model.LoggedInUser;
import com.example.feander.SignInAndUp.ui.loginsignup.ViewModel;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class Repository {

    private static volatile Repository instance;

    private DataSource dataSource;
    final String fileName = "user";
    private ViewModel viewModel;
    private Context context;
    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    public Repository() {

    }

    // private constructor : singleton access
    private Repository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static Repository getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new Repository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void logOut() {
        user = null;
        deleteLoggedInUser();
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
        observe(resultLive);

        //        if (resultLive.getValue() instanceof Result.Success) {
//            setLoggedInUser(((Result.Success<LoggedInUser>) resultLive.getValue()).getData());
//        }
        return resultLive;
    }

    private void observe(final MutableLiveData<Result> resultLive) {
        resultLive.observeForever(new Observer<Result>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
    }

    public MutableLiveData<Result> signUp(String username, String password, String phoneNumbers) {
        final MutableLiveData<Result> resultLive = dataSource.signUp(username, password, phoneNumbers);
        observe(resultLive);
        return resultLive;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void storeLoggedInUser(String fileContents) {
        try (@SuppressLint("RestrictedApi") FileOutputStream fos = viewModel.getCallingActivity().openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(fileContents.getBytes());
        } catch (Exception e) {
            Log.d("write loggedinuser", "that bai");
        }
    }
    private void deleteLoggedInUser(){
        try{
            File file = new File(context.getFilesDir(), fileName);
            file.delete();
        }catch (Exception e){
            Log.d("delete loggedinuser", "that bai");

        }
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}