package com.example.feander.User.ui.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.feander.R;
import com.example.feander.User.data.DataSource;
import com.example.feander.User.data.Result;

public class UpdateActivity extends AppCompatActivity {
    private String userName;
    private ProgressBar progressBarUpdate, progressBarChange;
    EditText userNameEditText, oldPassword, newPassword;
    Button updateButton;
    Button changePasswodButton;
    EditText phoneNumberEditText;
    MutableLiveData<State> stateLiveData = new MutableLiveData<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        userNameEditText = findViewById(R.id.username);
        oldPassword = findViewById(R.id.old_password);
        newPassword = findViewById(R.id.new_password);
        updateButton = findViewById(R.id.update_button);
        progressBarUpdate = findViewById(R.id.update_progressbar);
        progressBarChange = findViewById(R.id.change_password_progressbar);
        userName = getIntent().getStringExtra("userName");
        changePasswodButton = findViewById(R.id.change_password_button);
        phoneNumberEditText = findViewById(R.id.phone_number);
        userNameEditText.setText(userName);
        oldPassword.setError(getString(R.string.invalid_password));
        newPassword.setError(getString(R.string.invalid_password));
        changePasswodButton.setEnabled(false);
        stateLiveData.observe(this, new Observer<State>() {
            @Override
            public void onChanged(@Nullable State state) {
                if (state == null) {
                    return;
                }
                changePasswodButton.setEnabled(state.isDataValid());
                if (state.getUsernameError() != null) {
                    oldPassword.setError(getString(state.getUsernameError()));
                }
                if (state.getPasswordError() != null) {
                    newPassword.setError(getString(state.getPasswordError()));
                }
            }
        });
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
                validData();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
                validData();
            }

            @Override
            public void afterTextChanged(Editable s) {
                validData();
            }
        };
        oldPassword.addTextChangedListener(afterTextChangedListener);
        newPassword.addTextChangedListener(afterTextChangedListener);
    }

    public void updateUser(View view) {
        progressBarUpdate.setVisibility(View.VISIBLE);
        final MutableLiveData<Result> resultMutableLiveData = new DataSource().updateUserData(userName, phoneNumberEditText.getText().toString().trim());
        resultMutableLiveData.observe(this, new Observer<Result>() {
            @Override
            public void onChanged(Result result) {
                if (result instanceof Result.Success) {
                    showResult("Cập nhật thành công");
                    progressBarUpdate.setVisibility(View.GONE);
                } else if (result instanceof Result.Error) {
                    showResult("Cập nhật thất bại");
                    progressBarUpdate.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showResult(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    private void validData() {
        if (!isPasswordValid(oldPassword.getText().toString().trim())) {
            stateLiveData.setValue(new State(R.string.invalid_password, null));
        } else if (!isPasswordValid(newPassword.getText().toString().trim())) {
            stateLiveData.setValue(new State(null, R.string.invalid_password));
        } else {
            stateLiveData.setValue(new State(true));
        }
    }


    public void changePassword(View view) {
        progressBarChange.setVisibility(View.VISIBLE);
        final MutableLiveData<Result> resultMutableLiveData = new DataSource().changePassword(userName, oldPassword.getText().toString().trim(), newPassword.getText().toString().trim());
        resultMutableLiveData.observe(this, new Observer<Result>() {
            @Override
            public void onChanged(Result result) {
                if (result instanceof Result.Success) {
                    showResult("Cập nhật mật khẩu thành công");
                    progressBarChange.setVisibility(View.GONE);
                } else if (result instanceof Result.Error) {
                    showResult(((Result.Error) result).getError().toString());
                    progressBarChange.setVisibility(View.GONE);
                }
            }
        });

    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


}