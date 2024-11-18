package com.onion.backend.service;

import com.onion.backend.entity.User;
import com.onion.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String name, String password, String email){
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setEmail(email);
        return userRepository.save(user);
    }

}
