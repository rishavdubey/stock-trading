package com.example.stocktrading.service;

import com.example.stocktrading.config.PasswordEncoder;
import com.example.stocktrading.entity.UserDetails;
import com.example.stocktrading.exception.CustomException;
import com.example.stocktrading.repository.UserRepository;
import com.example.stocktrading.request.user.UserLogInRequest;
import com.example.stocktrading.request.user.UserSingUpRequest;
import com.example.stocktrading.response.balance.BalanceResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;

@Service

public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public String addUser(UserSingUpRequest user) {
        String email = user.getEmail();
        if (!validateEmail(email)) {

            throw new CustomException("Please try with Valid Email!", "emailError");
        }
        // Check if the user already exists
        if (userRepository.existsById(email)) {
            throw new CustomException("User already exit for email :: " + email);
        }
        // Save the new user
        UserDetails newUser = new UserDetails();
        newUser.setEmail(email);
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setName(user.getName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);

        Map<String, String> values = Map.of(
                "name", user.getName() );

        emailService.sendSimpleEmail(email,"Sign Up Successfully !!","signUpEmail.txt",values);
        return "User added successfully";

    }

    public String logInUser(UserLogInRequest user) {
        String email = user.getEmail();
        String password = user.getPassword();

        if (!validateEmail(email)) {
            throw new CustomException("Please try with Valid Email!", "emailError");
        }


        // Check if the user exists
        UserDetails existingUser = userRepository.findById(email).orElse(null);
        if (existingUser == null) {
            throw new CustomException("User does not exist for email :: " + email);
        }
        // Validate password
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new CustomException("Invalid password");
        }
        return "Login successful";
    }

    public UserDetails getUserById(String userId) {
        UserDetails existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser == null) {
            throw new CustomException("User does not exist for email/userId :: " + userId);
        }
        return existingUser;
    }


    private boolean validateEmail(String email) {
        final String EMAIL_PATTERN =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        return email != null && pattern.matcher(email).matches();

    }


    public void updateUser(UserDetails user) {
        userRepository.save(user);
    }

    public BalanceResponse getBalance(String userName) {
        UserDetails userDetails = getUserById(userName);
        BalanceResponse balanceResponse=new BalanceResponse();
        balanceResponse.setBalance(userDetails.getBalance());
        return balanceResponse;
    }

    @Transactional
    public BalanceResponse addBalance(String userName, BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException("Balance must be non-negative");
        }

        UserDetails userDetails = getUserById(userName);
        userDetails.setBalance(userDetails.getBalance().add(balance));
        userRepository.save(userDetails);

        BalanceResponse balanceResponse=new BalanceResponse();
        balanceResponse.setBalance(userDetails.getBalance());

        Map<String, String> values = Map.of(
                "firstName", userDetails.getName(),
                "amount",String.valueOf(balance),
                "newBalance",String.valueOf(userDetails.getBalance()),
                "date", String.valueOf(new Date())
        );

        emailService.sendSimpleEmail(userDetails.getEmail(), "Funds Added Successfully","amountAdded.txt",values);
        return balanceResponse;
    }
}
