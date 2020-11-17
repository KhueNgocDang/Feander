package com.example.feander.User.data.model;

import androidx.annotation.NonNull;

public class UncorrectException extends Exception {
    @NonNull
    @Override
    public String toString() {
        return "Tài khoản hoặc mật khẩu không đúng";
    }
}
