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
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<User> createUser(@Valid @RequestBody UserCreationRequest request) {
        // 비밀번호 해싱
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        // 닉네임 중복 검사
        if (userService.findUserByNickname(request.getNickname()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        // 이메일 중복 검사
        if (userService.findUserByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        // 사용자 등록
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "사용자 정보 수정", description = "기존 사용자의 정보 수정")
    @PutMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId, @Valid @RequestBody UserCreationRequest request) {
        // 비밀번호 해싱
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        // 사용자 수정
        User user = userService.updateUser(userId, request);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "사용자 삭제", description = "특정 사용자 삭제")
    @DeleteMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "사용자 조회", description = "특정 사용자 조회")
    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<User> findUser(@PathVariable("userId") Long userId) {
        User user = userService.findUser(userId);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "사용자 로그인", description = "사용자 로그인을 수행하여 성공시 사용자 정보 반환")
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String loginId, @RequestParam String password) {
        Optional<User> userOptional = userService.loginUser(loginId, password);
        return userOptional.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    // 비밀번호 유효성 검사 메서드
    private boolean isValidPassword(String password) {
        // 최소 8자 이상, 대문자와 소문자, 숫자, 특수 문자를 포함해야 함
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // 이메일 형식 검사 메서드
    private boolean isValidEmail(String email) {
        // 간단한 이메일 형식 검사를 위한 정규식 사용
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
