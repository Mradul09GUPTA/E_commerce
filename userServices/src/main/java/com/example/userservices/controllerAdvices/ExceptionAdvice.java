package com.example.userservices.controllerAdvices;

import com.example.userservices.exceptions.InvalidToken;
import com.example.userservices.exceptions.PasswordInvalid;
import com.example.userservices.exceptions.UserNotFound;
import com.example.userservices.exceptions.UserPresent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler
    public ResponseEntity<String> handleUserPresentExpection(UserPresent userPresent){
        return new ResponseEntity<>(userPresent.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<String> handleUserNameInvalid(UserNotFound userPresent){
        return new ResponseEntity<>(userPresent.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<String> handlePasswordInvalid(PasswordInvalid passwordInvalid){
        return  new  ResponseEntity<>(passwordInvalid.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<String> handleTokenInvlaid(InvalidToken invalidToken){
        return  new  ResponseEntity<>(invalidToken.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
