package com.example.userservices.controllers;

import com.example.userservices.dtos.SignupUserDto;
import com.example.userservices.dtos.UserDto;
import com.example.userservices.dtos.LoginUserDto;
import com.example.userservices.exceptions.InvalidToken;
import com.example.userservices.exceptions.PasswordInvalid;
import com.example.userservices.exceptions.UserNotFound;
import com.example.userservices.exceptions.UserPresent;
import com.example.userservices.services.AuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<UserDto> login(@RequestBody LoginUserDto loginUserDto) throws UserNotFound, PasswordInvalid {

        Pair<UserDto,String> response= authService.login(loginUserDto);
        HttpHeaders headers= new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE,response.b);
        return new ResponseEntity<>(response.a,headers,HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupUserDto signupUserDto) throws UserPresent {

        UserDto userDto=authService.createUser(signupUserDto);
        return  new ResponseEntity<>(userDto,HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Boolean> verifytoken(@RequestBody String token) throws InvalidToken {

        Boolean validToken = authService.verifyToken(token);
        return new ResponseEntity<>(validToken,HttpStatus.OK);
    }

}
