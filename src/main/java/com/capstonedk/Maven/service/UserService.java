package com.capstonedk.Maven.service;

import com.capstonedk.Maven.model.User;
import com.capstonedk.Maven.repository.UserRepository;
import com.capstonedk.Maven.model.request.LoginRequest;
import com.capstonedk.Maven.model.response.LoginResponse;
import com.capstonedk.Maven.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public User createUser(String loginId, String password, String nickname) {
        if (isLoginIdDuplicate(loginId)) {
            throw new IllegalArgumentException("중복된 아이디입니다.");
        }

        if (isNicknameDuplicate(nickname)) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 하며, 영문 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
        }

        User user = new User();
        user.setLoginId(loginId);
        user.setPassword(passwordEncoder.encode(password));  // 비밀번호 해싱
        user.setNickname(nickname);
        return userRepository.save(user);
    }

    public User updateUser(Long userId, String loginId, String password, String nickname) {
        User user = findUser(userId);

        if (!user.getLoginId().equals(loginId) && isLoginIdDuplicate(loginId)) {
            throw new IllegalArgumentException("중복된 아이디입니다.");
        }

        if (!user.getNickname().equals(nickname) && isNicknameDuplicate(nickname)) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 하며, 영문 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.");
        }

        user.setLoginId(loginId);
        user.setPassword(passwordEncoder.encode(password));  // 비밀번호 해싱
        user.setNickname(nickname);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EmptyResultDataAccessException("해당 ID를 가진 사용자를 찾을 수 없습니다: " + userId, 1);
        }
    }

    public User findUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new EmptyResultDataAccessException("데이터베이스에 사용자가 없습니다", 1));
    }

    public Optional<User> findUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    public Optional<User> findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }

    public List<User> findAllUsersExceptUser(Long userId) {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> !user.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public boolean isLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByLoginId(request.getLoginId());
        if (userOptional.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOptional.get().getPassword())) {
            throw new IllegalArgumentException("Invalid loginId or password");
        }

        User user = userOptional.get();
        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        LoginResponse.Tokens tokens = new LoginResponse.Tokens(accessToken, refreshToken);
        return new LoginResponse(true, "LOGIN_SUCCESS", tokens, "로그인에 성공했습니다.");
    }
}
