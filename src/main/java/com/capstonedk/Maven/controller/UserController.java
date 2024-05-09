package com.capstonedk.Maven.controller;

import com.capstonedk.Maven.model.User;
import com.capstonedk.Maven.service.UserService;
import com.capstonedk.Maven.model.request.UserCreationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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
    public ResponseEntity<User> createUser(@RequestBody UserCreationRequest request) {
        try {
            // 중복된 로그인 ID가 있는지 확인
            Optional<User> existingUser = userService.findUserByLoginId(request.getLoginId());
            if (existingUser.isPresent()) {
                // 중복된 로그인 ID가 이미 존재하는 경우 회원가입 거부
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // 중복된 로그인 ID가 없는 경우 사용자 등록 수행
            User user = userService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "사용자 정보 수정", description = "기존 사용자의 정보 수정")
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId, @RequestBody UserCreationRequest request) {
        try {
            // 기존 사용자의 로그인 ID 가져오기
            User existingUser = userService.findUser(userId);
            String existingLoginId = existingUser.getLoginId();

            // 수정할 로그인 ID가 기존 사용자의 로그인 ID와 중복되는지 확인
            if (!existingLoginId.equals(request.getLoginId())) {
                // 수정할 로그인 ID와 기존 사용자의 로그인 ID가 다르면 중복되지 않는 것 확인
                Optional<User> existingUserWithNewLoginId = userService.findUserByLoginId(request.getLoginId());
                if (existingUserWithNewLoginId.isPresent()) {
                    // 수정할 로그인 ID가 이미 존재하는 경우 수정 거부
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            }

            // 중복된 로그인 ID가 없는 경우 사용자 정보 수정 수행
            User user = userService.updateUser(userId, request);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "사용자 삭제", description = "특정 사용자 삭제")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // 예외 처리
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
            // 예외 처리
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "사용자 로그인", description = "사용자 로그인을 수행하여 성공시 사용자 정보 반환")
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String loginId, @RequestParam String password) {
        Optional<User> userOptional = userService.loginUser(loginId, password);
        return userOptional.map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }
}