package com.example.userservices.services;

import com.example.userservices.dtos.LoginUserDto;
import com.example.userservices.dtos.SignupUserDto;
import com.example.userservices.dtos.UserDto;
import com.example.userservices.exceptions.InvalidToken;
import com.example.userservices.exceptions.PasswordInvalid;
import com.example.userservices.exceptions.UserNotFound;
import com.example.userservices.exceptions.UserPresent;
import com.example.userservices.models.Role;
import com.example.userservices.models.Session;
import com.example.userservices.models.State;
import com.example.userservices.models.User;
import com.example.userservices.repos.SessionRepo;
import com.example.userservices.repos.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
public class AuthService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder ;
    @Autowired
    private SecretKey secretKey;
    @Autowired
    private SessionRepo  sessionRepo;

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

    public Pair<UserDto,String> login(LoginUserDto loginUserDto) throws UserNotFound, PasswordInvalid {
        String email = loginUserDto.getEmail().trim().toLowerCase();
        Optional<User> opUser= userRepo.findByEmail(email);
        if(opUser.isEmpty()){
            throw  new UserNotFound("User not found. Please correct your username ");
        }
        User user= opUser.get();
        String encodedPassword = user.getPassword();
        if(!bCryptPasswordEncoder.matches(loginUserDto.getPassword(),encodedPassword)){
            throw  new PasswordInvalid("Password is Invalid");
        }
        Map<String,Object>payload = new HashMap<>();
        payload.put("User",from(user));
        payload.put("iat",System.currentTimeMillis());
        payload.put("exp",System.currentTimeMillis()+1000000);
        payload.put("issueBy","Zx");

        String token = Jwts.builder()
                .claims(payload)//payload
                .signWith(secretKey)//sign
                .compact();//toString

        addTokenInSession(token,user);


        return new Pair<>(from(user),token);
    }

    public Boolean verifyToken(String token) throws InvalidToken {
        Optional<Session> opSession = sessionRepo.findByToken(token);
        if(opSession.isEmpty()){
            throw  new InvalidToken("Token is Inavlid");
        }
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims claims = jwtParser.parseSignedClaims(token).getPayload();
        Long exp= (Long) claims.get("exp");
        if(exp<System.currentTimeMillis()){
            Session session = opSession.get();
            session.setState(State.INACTIVE);
            sessionRepo.save(session);
            throw  new InvalidToken("Token Expired");
        }
        return true;
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
        user.setState(State.ACTIVE);
        return user;
    }
    private List<Role> addDefaultRoles(){
        List<Role> roles=new ArrayList<>();
        Role role=new Role();
        role.setValue("Default");
        role.setState(State.ACTIVE);
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
    private void addTokenInSession(String token,User user){
        Session session=new Session();
        session.setToken(token);
        session.setUser(user);
        session.setState(State.ACTIVE);
        sessionRepo.save(session);
    }

}
