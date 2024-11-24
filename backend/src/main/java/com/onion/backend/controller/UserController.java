package com.onion.backend.controller;

import com.onion.backend.dto.SignInUser;
import com.onion.backend.dto.SignUpUser;
import com.onion.backend.entity.User;
import com.onion.backend.jwt.JwtBlackListService;
import com.onion.backend.jwt.JwtUtils;
import com.onion.backend.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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

    @Value("${jwt.cookieName}")
    private String jwtCookieName;

    @Value("${jwt.expire}")
    private Integer jwtExpireTime;

    private final UserService userService;
    private final JwtBlackListService jwtBlackListService;
    private final JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
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
    public void login(@RequestBody SignInUser signInUser, HttpServletResponse response) {
        String jwt = userService.login(signInUser);
        Cookie cookie = new Cookie(jwtCookieName, jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(jwtExpireTime);
        response.addCookie(cookie);
    }

    @GetMapping("/token/validation")
    public ResponseEntity<Void> jwtValidate(@RequestParam("token") String token) {
        if (userService.validateToken(token)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(jwtCookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    @PostMapping("/logout/all")
    public void logoutAll(
        @RequestParam(required = false, value = "requestToken") String requestToken,
        HttpServletRequest request, HttpServletResponse response) {

        String headerToken = jwtUtils.resolveToken(request);
        String token = StringUtils.hasText(requestToken) ? requestToken : headerToken;

        LocalDateTime now = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());

        String email = jwtUtils.getUsernameFromToken(token);
        jwtBlackListService.addToBlackList(token, now, email);

        Cookie cookie = new Cookie(jwtCookieName, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
