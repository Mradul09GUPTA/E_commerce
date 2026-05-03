package com.example.userservices.services;

import com.example.userservices.dtos.SignupUserDto;
import com.example.userservices.dtos.UserDto;
import com.example.userservices.exceptions.UserPresent;
import com.example.userservices.models.Role;
import com.example.userservices.models.User;
import com.example.userservices.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class AuthService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder ;

    public UserDto createUser(SignupUserDto signupUserDto) throws UserPresent {
       String email = signupUserDto.getEmail().trim().toLowerCase();
        Optional<User> opUser= userRepo.findByEmail(email);
        if(opUser.isPresent()){
            throw new UserPresent("User alredy exist in the system ");
        }
        User user = from (signupUserDto);
        userRepo.save(user);
        UserDto userDto = from(user);
        return userDto;
    }
    private User from(SignupUserDto signupUserDto) {
        User user = new User();
        user.setName(signupUserDto.getFirstName()+" "+signupUserDto.getLastName());
        String encodePassword = bCryptPasswordEncoder.encode(signupUserDto.getPassword());
        user.setPassword(encodePassword);
        user.setRoles(addDefaultRoles());
        user.setPhoneNumber(signupUserDto.getPhoneNumber());
        String email = signupUserDto.getEmail().trim().toLowerCase();
        user.setEmail(email);
        return user;
    }
    private List<Role> addDefaultRoles(){
        List<Role> roles=new ArrayList<>();
        Role role=new Role();
        role.setValue("Default");
        roles.add(role);
        return roles;
    }
    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRole(convertListToString(user.getRoles()));
        userDto.setStatus("ACTIVE");
        return userDto;
    }
    private List<String> convertListToString(List<Role> roles){
        List<String> list=new ArrayList<>();
        for(Role role:roles){
            list.add(role.getValue());
        }
        return list;
    }


}
