package com.example.feander.User.data.model;

import androidx.annotation.NonNull;

public class UserNameExistException extends Exception {
    @NonNull
    @Override
    public String toString() {
        return "Tên tài khoản này đã được đăng ký";
    }
}
