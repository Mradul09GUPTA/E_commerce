package com.example.userservices.services;

import com.example.userservices.dtos.UserDto;
import com.example.userservices.exceptions.UserNotFound;
import com.example.userservices.models.Role;
import com.example.userservices.models.User;
import com.example.userservices.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
     public UserDto getUserById(Long userId) throws UserNotFound {
        Optional<User> opUser= userRepo.findById(userId);
        if(opUser.isEmpty()){
            throw  new UserNotFound("User Not found");
        }
        return from(opUser.get());
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
