package com.example.userservices.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
}
