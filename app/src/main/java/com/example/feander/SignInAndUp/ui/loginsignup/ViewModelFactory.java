package com.example.feander.SignInAndUp.ui.loginsignup;

import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.example.feander.SignInAndUp.data.DataSource;
import com.example.feander.SignInAndUp.data.Repository;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends androidx.lifecycle.ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModel.class)) {
            return (T) new ViewModel(Repository.getInstance(new DataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}