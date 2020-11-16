package com.example.feander.User.ui.user;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.feander.MainActivity;
import com.example.feander.R;
import com.example.feander.User.data.Result;
import com.example.feander.User.data.model.LoggedInUser;

public class LoginActivity extends AppCompatActivity {

    private ViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory())
                .get(ViewModel.class);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.phone_number);
        final Button loginButton = findViewById(R.id.update_button);
        final ProgressBar loadingProgressBar = findViewById(R.id.progres_bar);

        viewModel.getState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state == null) {
                    return;
                }
                loginButton.setEnabled(state.isDataValid());
                if (state.getUsernameError() != null) {
                    usernameEditText.setError(getString(state.getUsernameError()));
                }
                if (state.getPasswordError() != null) {
                    passwordEditText.setError(getString(state.getPasswordError()));
                }
            }
        });
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    viewModel.login(usernameEditText.getText().toString(),
//                            passwordEditText.getText().toString());
//                }
//                return false;
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                viewModel.setCallingActivity(LoginActivity.this);
                final MutableLiveData<Result> resultLive = viewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                resultLive.observeForever(new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        if (result instanceof Result.Success) {
                            loadingProgressBar.setVisibility(View.GONE);
                            updateUiWithUser(((Result.Success<LoggedInUser>) result).getData());
                            resultLive.removeObserver(this);
                            finish();
                        } else if (result instanceof Result.Error) {
                            loadingProgressBar.setVisibility(View.GONE);
                            showLoginFailed(((Result.Error) result).getError().toString());
                            resultLive.setValue(null);
                        }
                    }
                });

            }
        });
    }

    private void updateUiWithUser(LoggedInUser model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        openMainIntent(model.getDisplayName());
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
    }

    public void openSignUp(View view) {
        Intent intent = new Intent(this, SignUp_Activity.class);
        startActivity(intent);
    }

    public void openMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openMainIntent(String userIntents) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userName", userIntents);
        startActivity(intent);
    }
}