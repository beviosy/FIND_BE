package com.capstonedk.Maven.controller;


import com.capstonedk.Maven.model.User;
import com.capstonedk.Maven.model.request.UserCreationRequest;
import com.capstonedk.Maven.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "signup", description = "회원가입 API")
public class UserController {
    private final StoreService storeService;

    @Autowired
    public UserController(StoreService storeService) {
        this.storeService = storeService;
    }

    @Operation(summary = "사용자 등록", description = "새로운 사용자를 등록하여 회원가입 진행")
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody UserCreationRequest request) {
        User user = storeService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "사용자 정보 수정", description = "기존 사용자의 정보 수정")
    @PutMapping("/{userID}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserCreationRequest request) {
        User user = storeService.updateUser(userId, request);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "사용자 삭제", description = "특정 사용자 삭제")
    @DeleteMapping("/{userID}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        storeService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "사용자 조회", description = "특정 사용자 조회")
    @GetMapping("/{userID}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        User user = storeService.findUser(userId);
        return ResponseEntity.ok(user);
    }

}