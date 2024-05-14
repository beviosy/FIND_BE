package com.capstonedk.Maven.controller;

import com.capstonedk.Maven.model.Review;
import com.capstonedk.Maven.model.User;
import com.capstonedk.Maven.model.request.LoginRequest;
import com.capstonedk.Maven.model.request.RegisterRequest;
import com.capstonedk.Maven.model.request.UpdateUserRequest;
import com.capstonedk.Maven.model.response.ApiResponse;
import com.capstonedk.Maven.model.response.LoginResponse;
import com.capstonedk.Maven.model.response.UserProfileResponse;
import com.capstonedk.Maven.service.ReviewService;
import com.capstonedk.Maven.service.UserService;
import com.capstonedk.Maven.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

    @Operation(summary = "사용자 등록", description = "새로운 사용자를 등록합니다. 비밀번호는 최소 8자 이상이어야 하며, 영문 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterRequest request) {
        try {
            User user = userService.createUser(request.getLoginId(), request.getPassword(), request.getNickname());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "USER_CREATED", "사용자가 성공적으로 생성되었습니다", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "USER_CREATION_FAILED", e.getMessage(), null));
        }
    }

    @Operation(summary = "로그인 ID 중복 확인", description = "로그인 ID의 중복 여부를 확인합니다.")
    @GetMapping("/check-id")
    public ResponseEntity<ApiResponse> checkLoginIdDuplicate(@RequestParam String loginId) {
        boolean isDuplicate = userService.isLoginIdDuplicate(loginId);
        return ResponseEntity.ok(new ApiResponse(!isDuplicate, "CHECK_ID", isDuplicate ? "중복된 아이디입니다." : "사용 가능한 아이디입니다.", null));
    }

    @Operation(summary = "닉네임 중복 확인", description = "닉네임의 중복 여부를 확인합니다.")
    @GetMapping("/check-nickname")
    public ResponseEntity<ApiResponse> checkNicknameDuplicate(@RequestParam String nickname) {
        boolean isDuplicate = userService.isNicknameDuplicate(nickname);
        return ResponseEntity.ok(new ApiResponse(!isDuplicate, "CHECK_NICKNAME", isDuplicate ? "중복된 닉네임입니다." : "사용 가능한 닉네임입니다.", null));
    }

    @Operation(summary = "비밀번호 규칙 검사", description = "비밀번호가 규칙에 맞는지 확인합니다. 비밀번호는 최소 8자 이상이어야 하며, 영문 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
    @GetMapping("/check-password")
    public ResponseEntity<ApiResponse> checkPasswordValidity(@RequestParam String password) {
        boolean isValid = userService.isValidPassword(password);
        return ResponseEntity.ok(new ApiResponse(isValid, "CHECK_PASSWORD", isValid ? "비밀번호가 유효합니다." : "비밀번호가 유효하지 않습니다.", null));
    }

    @Operation(summary = "로그인", description = "사용자가 로그인합니다.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            if (response.getResult() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(false, "LOGIN_FAILED", null, "잘못된 로그인 정보입니다"));
            }

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + response.getResult().getAccessToken())
                    .header("Refresh-Token", response.getResult().getRefreshToken())
                    .body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(false, "LOGIN_FAILED", null, "잘못된 로그인 정보입니다"));
        }
    }

    @Operation(summary = "마이페이지", description = "로그인한 사용자의 정보를 조회합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getUserDetails(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ") && jwtUtil.validateToken(token.substring(7))) {
            String username = jwtUtil.getUsernameFromToken(token.substring(7));
            Optional<User> userOptional = userService.findUserByLoginId(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                List<Review> reviews = reviewService.findReviewsByUserId(user.getUserId());
                UserProfileResponse response = new UserProfileResponse(user, reviews);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "액세스 토큰 재발급", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급받습니다.")
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse.Tokens> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("Refresh-Token");
        if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            Optional<User> user = userService.findUserByLoginId(username);
            if (user.isPresent()) {
                String newAccessToken = jwtUtil.generateAccessToken(user.get());
                return ResponseEntity.ok(new LoginResponse.Tokens(newAccessToken, refreshToken));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "사용자 수정", description = "로그인한 사용자의 정보를 수정합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(
            HttpServletRequest request,
            @RequestBody UpdateUserRequest updateUserRequest) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ") && jwtUtil.validateToken(token.substring(7))) {
            String username = jwtUtil.getUsernameFromToken(token.substring(7));
            Optional<User> existingUser = userService.findUserByLoginId(username);
            if (existingUser.isPresent() && existingUser.get().getLoginId().equals(updateUserRequest.getLoginId())) {
                User updatedUser = userService.updateUser(existingUser.get().getUserId(), updateUserRequest.getLoginId(), updateUserRequest.getPassword(), updateUserRequest.getNickname());
                return ResponseEntity.ok(updatedUser);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "사용자 삭제", description = "로그인한 사용자의 계정을 삭제합니다.")
    @SecurityRequirement(name = "Bearer Authentication")
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
