package com.example.feander.User.ui.user;

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
import com.example.feander.User.data.Result;
import com.example.feander.User.data.model.LoggedInUser;


public class SignUp_Activity extends AppCompatActivity {
    ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);
        viewModel = ViewModelProviders.of(this, new ViewModelFactory())
                .get(ViewModel.class);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.phone_number);
        final EditText phoneNumbersEditText = findViewById(R.id.phoneNumber);
        final Button signUpButton = findViewById(R.id.update_button);
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
                final MutableLiveData<Result> resultLive = viewModel.signUp(usernameEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim(), phoneNumbersEditText.getText().toString().trim());
                resultLive.observeForever(new Observer<Result>() {
                    @Override
                    public void onChanged(Result result) {
                        if (result instanceof Result.Success) {
                            progressBar.setVisibility(View.GONE);
                            updateUiWithUser(((Result.Success<LoggedInUser>) result).getData());
                            resultLive.removeObserver(this);
                            finish();
                        } else if (result instanceof Result.Error) {
                            progressBar.setVisibility(View.GONE);
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
        intent.putExtra("userName", displayName);
        startActivity(intent);
    }

    public void openSignIn(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
