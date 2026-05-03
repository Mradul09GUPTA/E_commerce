package com.example.userservices.exceptions;

public class InvalidToken extends Exception {
    public InvalidToken(String tokenIsInavlid) {
        super(tokenIsInavlid);
    }
}
