package com.onion.backend.controller;

import com.onion.backend.dto.SignInUser;
import com.onion.backend.dto.SignUpUser;
import com.onion.backend.entity.User;
import com.onion.backend.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/sign-up")
    public ResponseEntity<User> createUser(@RequestBody SignUpUser signUpUser) {
        return ResponseEntity.ok(userService.createUser(signUpUser));
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "유저 엔티티의 PK", required = true) @PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody SignInUser signInUser){
        return ResponseEntity.ok(userService.login(signInUser));
    }

    @GetMapping("/token/validation")
    public ResponseEntity<Void> jwtValidate(@RequestParam("token") String token){
        if(userService.validateToken(token)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

}
