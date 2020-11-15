package com.example.feander.SignInAndUp.ui.loginsignup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.os.Build;
import android.util.Patterns;

import com.example.feander.R;
import com.example.feander.SignInAndUp.data.Repository;
import com.example.feander.SignInAndUp.data.Result;

public class ViewModel extends androidx.lifecycle.ViewModel {
    private AppCompatActivity callingActivity;

    private MutableLiveData<State> state = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private Repository repository;

    ViewModel(Repository repository) {
        this.repository = repository;
        this.repository.setViewModel(this);
    }

    public LiveData<State> getState() {
        return state;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MutableLiveData<Result> login(String username, String password) {
        // can be launched in a separate asynchronous job
        MutableLiveData<Result> resultLive = repository.login(username, password);

//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
//            storeLoggedInUser(username);
//            Intent intent = new Intent(getCallingActivity(), MainActivity.class);
//            intent.putExtra("userName", data.getDisplayName());
//            getCallingActivity().startActivity(intent);
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
        return resultLive;
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            state.setValue(new State(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            state.setValue(new State(null, R.string.invalid_password));
        } else {
            state.setValue(new State(true));
        }
    }

    public MutableLiveData<Result> signUp(String username, String password, String phoneNumbers) {
        return repository.signUp(username, password, phoneNumbers);

        // can be launched in a separate asynchronous job
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public void setCallingActivity(AppCompatActivity callingActivity) {
        this.callingActivity = callingActivity;
    }

    public AppCompatActivity getCallingActivity() {
        return callingActivity;
    }
}