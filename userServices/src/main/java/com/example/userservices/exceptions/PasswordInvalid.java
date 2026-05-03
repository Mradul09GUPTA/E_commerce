package com.example.userservices.exceptions;

public class PasswordInvalid extends Exception {
    public PasswordInvalid(String passwordIsInvalid) {
        super(passwordIsInvalid);
    }
}
