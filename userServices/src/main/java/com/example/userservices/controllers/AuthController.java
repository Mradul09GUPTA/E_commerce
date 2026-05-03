package com.example.userservices.controllers;

import com.example.userservices.dtos.SignupUserDto;
import com.example.userservices.dtos.UserDto;
import com.example.userservices.dtos.LoginUserDto;
import com.example.userservices.exceptions.UserPresent;
import com.example.userservices.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<String> login(LoginUserDto userDto){


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupUserDto signupUserDto) throws UserPresent {

        UserDto userDto = authService.createUser(signupUserDto);
        return new ResponseEntity<>(userDto,HttpStatus.CREATED);
    }

}
