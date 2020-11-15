package com.example.feander.SignInAndUp.ui.loginsignup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.feander.MainActivity;
import com.example.feander.R;
import com.example.feander.SignInAndUp.data.Result;
import com.example.feander.SignInAndUp.data.model.LoggedInUser;


public class SignUp_Activity extends AppCompatActivity {
    ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory())
                .get(ViewModel.class);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText phoneNumbersEditText = findViewById(R.id.phoneNumber);
        final Button signUpButton = findViewById(R.id.button);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        viewModel.getState().observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state == null) {
                    return;
                }
                signUpButton.setEnabled(state.isDataValid());
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
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                viewModel.setCallingActivity(SignUp_Activity.this);
                final MutableLiveData<Result> resultLive = viewModel.signUp(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), phoneNumbersEditText.getText().toString());
                resultLive.observeForever(new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        progressBar.setVisibility(View.GONE);
                        if (result instanceof Result.Success) {
                            updateUiWithUser(((Result.Success<LoggedInUser>) result).getData());
                            resultLive.removeObserver(this);
                            finish();
                        } else if (result instanceof Result.Error) {
                            showSignUpFailed(((Result.Error) result).getError().toString());
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

    private void showSignUpFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_LONG).show();
    }

    private void openMainIntent(String displayName) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user", displayName);
        startActivity(intent);
    }

    public void openSignIn(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
