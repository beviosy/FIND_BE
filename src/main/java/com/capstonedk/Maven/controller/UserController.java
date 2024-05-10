package com.capstonedk.Maven.controller;

import com.capstonedk.Maven.model.User;
import com.capstonedk.Maven.service.UserService;
import com.capstonedk.Maven.model.request.UserCreationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "회원가입/로그인 API")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "사용자 등록", description = "새로운 사용자를 등록하여 회원가입 진행")
    @PostMapping("/register")
    public ResponseEntity<User> createUser(
            @Valid @RequestBody UserCreationRequest request) {
        try {
            // 비밀번호 해싱
            request.setPassword(passwordEncoder.encode(request.getPassword()));

            // 사용자 등록
            User user = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "사용자 정보 수정", description = "기존 사용자의 정보 수정")
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable("userId") Long userId,
            @RequestParam String loginId,
            @RequestParam String password,
            @RequestParam String nickname,
            @RequestParam String email) {
        try {
            UserCreationRequest request = new UserCreationRequest();
            request.setLoginId(loginId);
            request.setPassword(password);
            request.setNickname(nickname);
            request.setEmail(email);

            // 비밀번호 해싱
            request.setPassword(passwordEncoder.encode(request.getPassword()));

            // 사용자 수정
            User user = userService.updateUser(userId, request);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "사용자 삭제", description = "특정 사용자 삭제")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "사용자 조회", description = "특정 사용자 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<User> findUser(@PathVariable("userId") Long userId) {
        try {
            User user = userService.findUser(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "사용자 로그인", description = "사용자 로그인을 수행하여 성공시 사용자 정보 반환")
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String loginId, @RequestParam String password) {
        Optional<User> userOptional = userService.loginUser(loginId, password);
        return userOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}

