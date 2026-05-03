package com.example.userservices.controllerAdvices;

import com.example.userservices.exceptions.UserPresent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdiver {
    @ExceptionHandler
    public ResponseEntity<String> handleUserPresentExpection(UserPresent userPresent){
        return new ResponseEntity<>(userPresent.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
