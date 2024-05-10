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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "사용자 등록", description = "새로운 사용자를 등록하여 회원가입 진행")
    @PostMapping("/register")
    public ResponseEntity<User> createUser(
            @Parameter(description = "아이디", required = true) @RequestParam String loginId,
            @Parameter(description = "비밀번호  최소 8자 이상, 대문자와 소문자, 숫자, 특수 문자를 포함해야 함", required = true) @RequestParam String password,
            @Parameter(description = "닉네임", required = true) @RequestParam String nickname,
            @Parameter(description = "이메일", required = true) @RequestParam String email) {
        try {
            // 비밀번호 규칙 검사
            if (!isValidPassword(password)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // 이메일 형식 검사
            if (!isValidEmail(email)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            // 닉네임 중복 검사
            if (userService.findUserByNickname(nickname).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }

            // 이메일 중복 검사
            if (userService.findUserByEmail(email).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }

            UserCreationRequest request = new UserCreationRequest();
            request.setLoginId(loginId);
            request.setPassword(password);
            request.setNickname(nickname);
            request.setEmail(email);

            // 중복된 로그인 ID가 있는지 확인
            boolean existingUser = userService.findUserByLoginId(request.getLoginId()).isPresent();
            if (existingUser) {
                // 중복된 로그인 ID가 이미 존재하는 경우 회원가입 거부
                return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
            }

            // 중복된 로그인 ID가 없는 경우 사용자 등록 수행
            User user = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
