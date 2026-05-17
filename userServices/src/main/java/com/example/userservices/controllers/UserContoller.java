package com.example.userservices.controllers;

import com.example.userservices.dtos.UserDto;
import com.example.userservices.exceptions.UserNotFound;
import com.example.userservices.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserContoller {

 @Autowired
 private UserService userService;
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) throws UserNotFound {
        return  userService.getUserById(id);
    }
}
