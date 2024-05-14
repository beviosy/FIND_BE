package com.capstonedk.Maven.controller;

import com.capstonedk.Maven.model.User;
import com.capstonedk.Maven.model.request.LoginRequest;
import com.capstonedk.Maven.model.response.ApiResponse;
import com.capstonedk.Maven.model.response.LoginResponse;
import com.capstonedk.Maven.service.UserService;
import com.capstonedk.Maven.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "사용자 등록", description = "새로운 사용자를 등록합니다. 비밀번호는 최소 8자 이상이어야 하며, 영문 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(
            @RequestParam String loginId,
            @RequestParam String password,
            @RequestParam String nickname) {
        try {
            User user = userService.createUser(loginId, password, nickname);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "USER_CREATED", "사용자가 성공적으로 생성되었습니다", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "USER_CREATION_FAILED", e.getMessage(), null));
        }
    }

    @Operation(summary = "로그인 ID 중복 확인", description = "로그인 ID의 중복 여부를 확인합니다.")
    @GetMapping("/check-id")
    public ResponseEntity<ApiResponse> checkLoginIdDuplicate(@RequestParam String loginId) {
        boolean isDuplicate = userService.isLoginIdDuplicate(loginId);
        return ResponseEntity.ok(new ApiResponse(!isDuplicate, "CHECK_ID", "로그인 ID 확인이 완료되었습니다", null));
    }

    @Operation(summary = "닉네임 중복 확인", description = "닉네임의 중복 여부를 확인합니다.")
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse> checkNicknameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = userService.isNicknameDuplicate(nickname);
        return ResponseEntity.ok(new ApiResponse(!isDuplicate, "CHECK_NICKNAME", "닉네임 확인이 완료되었습니다", null));
    }

    @Operation(summary = "비밀번호 규칙 검사", description = "비밀번호가 규칙에 맞는지 확인합니다. 비밀번호는 최소 8자 이상이어야 하며, 영문 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    @GetMapping("/check-password")
    public ResponseEntity<ApiResponse> checkPasswordValidity(@RequestParam String password) {
        boolean isValid = userService.isValidPassword(password);
        return ResponseEntity.ok(new ApiResponse(isValid, "CHECK_PASSWORD", "비밀번호 확인이 완료되었습니다", null));
    }

    @Operation(summary = "로그인", description = "사용자가 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestParam String loginId,
            @RequestParam String password) {
        try {
            LoginResponse response = userService.login(new LoginRequest(loginId, password));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(false, "LOGIN_FAILED", null, "잘못된 로그인 정보입니다"));
        }
    }

    @Operation(summary = "사용자 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<User> getUserDetails(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ") && jwtUtil.validateToken(token.substring(7))) {
            String username = jwtUtil.getUsernameFromToken(token.substring(7));
            Optional<User> user = userService.findUserByLoginId(username);
            if (user.isPresent()) {
                return ResponseEntity.ok(user.get());
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "사용자 수정", description = "로그인한 사용자의 정보를 수정합니다.")
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(
            HttpServletRequest request,
            @RequestParam String loginId,
            @RequestParam String password,
            @RequestParam String nickname) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ") && jwtUtil.validateToken(token.substring(7))) {
            String username = jwtUtil.getUsernameFromToken(token.substring(7));
            Optional<User> existingUser = userService.findUserByLoginId(username);
            if (existingUser.isPresent()) {
                User updatedUser = userService.updateUser(existingUser.get().getUserId(), loginId, password, nickname);
                return ResponseEntity.ok(updatedUser);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "사용자 삭제", description = "로그인한 사용자의 계정을 삭제합니다.")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ") && jwtUtil.validateToken(token.substring(7))) {
            String username = jwtUtil.getUsernameFromToken(token.substring(7));
            Optional<User> existingUser = userService.findUserByLoginId(username);
            if (existingUser.isPresent()) {
                userService.deleteUser(existingUser.get().getUserId());
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
