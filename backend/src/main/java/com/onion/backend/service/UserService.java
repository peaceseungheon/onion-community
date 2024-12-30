package com.onion.backend.service;

import com.onion.backend.dto.SignInOut;
import com.onion.backend.dto.SignInUser;
import com.onion.backend.dto.SignUpUser;
import com.onion.backend.entity.User;
import com.onion.backend.jwt.JwtUtils;
import com.onion.backend.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User createUser(SignUpUser signUpUser){
        User user = new User();
        user.setName(signUpUser.getName());
        user.setPassword(passwordEncoder.encode(signUpUser.getPassword()));
        user.setEmail(signUpUser.getEmail());
        return userRepository.save(user);
    }

    public void deleteUser(Long userId){
        userRepository.deleteById(userId);
    }

    public SignInOut login(SignInUser signInUser) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            signInUser.getUserId(), signInUser.getPassword());
        Authentication authenticate = authenticationManager.authenticate(token);
        String jwt = jwtUtils.generateToken(authenticate.getName());
        User user = userRepository.getByEmail(signInUser.getUserId());
        return new SignInOut(jwt, user.getId(), user.getName());
    }

    public boolean validateToken(String token) {
        return jwtUtils.validateToken(token);
    }
}
