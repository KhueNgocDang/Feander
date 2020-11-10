package com.example.feander.SignInAndUp.data.model;

import androidx.annotation.NonNull;

public class UncorrectPassWordException extends Exception {
    @NonNull
    @Override
    public String toString() {
        return "Uncorrect PassWord";
    }
}
