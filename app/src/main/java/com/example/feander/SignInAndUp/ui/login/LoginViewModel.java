package com.example.feander.SignInAndUp.ui.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.os.Build;
import android.util.Patterns;

import com.example.feander.R;
import com.example.feander.SignInAndUp.data.LoginRepository;
import com.example.feander.SignInAndUp.data.Result;

public class LoginViewModel extends ViewModel {
    private AppCompatActivity callingActivity;

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
        this.loginRepository.setLoginViewModel(this);
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MutableLiveData<Result> login(String username, String password) {
        // can be launched in a separate asynchronous job
        MutableLiveData<Result> resultLive = loginRepository.login(username, password);

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
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
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
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void storeLoggedInUser(String fileContents) {
//        try (@SuppressLint("RestrictedApi") FileOutputStream fos = getCallingActivity().getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE)) {
//            fos.write(fileContents.getBytes());
//        } catch (Exception e) {
//            Log.d("loggedinuser", "that bai");
//        }
//    }
}