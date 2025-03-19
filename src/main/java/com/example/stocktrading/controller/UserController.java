package com.example.stocktrading.controller;

import com.example.stocktrading.common.CommonValidation;
import com.example.stocktrading.config.PasswordEncoder;
import com.example.stocktrading.config.SecretKeyProvider;
import com.example.stocktrading.request.user.UserLogInRequest;
import com.example.stocktrading.request.user.UserSingUpRequest;
import com.example.stocktrading.response.balance.BalanceResponse;
import com.example.stocktrading.response.CustomMessage;
import com.example.stocktrading.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/api/user")
class UserController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecretKeyProvider secretKeyProvider;

    @Autowired
    private UserService userService;
    @Autowired
    private CommonValidation commonValidation;


    @PostMapping("/signup")
    public ResponseEntity<CustomMessage> signup(@Valid @RequestBody UserSingUpRequest user)  {
        String message=userService.addUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomMessage(message,"200"));
    }


    @PostMapping("/login")
    public ResponseEntity<CustomMessage> login(@Valid @RequestBody UserLogInRequest user, HttpServletResponse response) {
        String message= userService.logInUser(user);
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiry
                .signWith(secretKeyProvider.getSecretKey())
                .compact();

        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);   /** 1 day expiry **/
        response.addCookie(cookie);
        return ResponseEntity.status(HttpStatus.OK).body(new CustomMessage(message,"200"));
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expire immediately
        response.addCookie(cookie);
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/balance")
    ResponseEntity<?> getBalance(@CookieValue(name = "JWT_TOKEN", required = false) String token){
        String userName=commonValidation.validateToken(token);
        BalanceResponse balanceResponse =userService.getBalance(userName);
        return ResponseEntity.status(HttpStatus.OK).body(balanceResponse);

    }

    @PostMapping("/balance")
    ResponseEntity<?> addBalance(@Valid @RequestParam("amount") BigDecimal amount, @CookieValue(name = "JWT_TOKEN", required = false) String token){
        String userName=commonValidation.validateToken(token);
        BalanceResponse balanceResponse =userService.addBalance(userName,amount);
        return ResponseEntity.status(HttpStatus.OK).body(balanceResponse);

    }



}