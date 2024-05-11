package com.capstonedk.Maven.service;

import com.capstonedk.Maven.model.User;
import com.capstonedk.Maven.repository.UserRepository;
import com.capstonedk.Maven.model.request.UserCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 스프링 시큐리티의 UserDetailsService를 구현
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자가 입력한 username을 사용하여 데이터베이스에서 사용자를 찾음
        User user = userRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 username을 가진 사용자를 찾을 수 없습니다: " + username));

        // 찾은 사용자 정보를 UserDetails 객체로 변환하여 반환
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLoginId())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    // 새 사용자 생성 메서드
    public User createUser(UserCreationRequest request) {
        // 요청된 정보를 바탕으로 새 User 객체를 생성하고 저장소에 저장
        User user = new User();
        BeanUtils.copyProperties(request, user);
        return userRepository.save(user);
    }

    // 사용자 정보 수정 메서드
    public User updateUser(Long userId, UserCreationRequest request) {
        // userId를 사용하여 데이터베이스에서 해당 사용자를 찾음
        User user = findUser(userId);
        // 요청된 정보로 사용자 객체를 업데이트하고 저장
        BeanUtils.copyProperties(request, user);
        return userRepository.save(user);
    }

    // 사용자 삭제 메서드
    public void deleteUser(Long userId) {
        try {
            // userId를 사용하여 데이터베이스에서 해당 사용자를 삭제
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException ex) {
            // 해당 ID로 사용자를 찾지 못한 경우 예외 처리
            throw new EmptyResultDataAccessException("해당 ID를 가진 사용자를 찾을 수 없습니다: " + userId, 1);
        }
    }

    // 사용자 조회 메서드
    public User findUser(Long userId) {
        // userId를 사용하여 데이터베이스에서 해당 사용자를 찾음
        Optional<User> user = userRepository.findById(userId);
        // 사용자를 찾지 못한 경우 예외 처리
        return user.orElseThrow(() -> new EmptyResultDataAccessException("데이터베이스에 사용자가 없습니다", 1));
    }

    // 로그인 사용자 조회 메서드
    public Optional<User> loginUser(String loginId, String password) {
        // 로그인 ID와 비밀번호를 사용하여 사용자를 찾음
        return userRepository.findByLoginIdAndPassword(loginId, password);
    }

    // 로그인 ID로 사용자 조회 메서드
    public Optional<User> findUserByLoginId(String loginId) {
        // 로그인 ID를 사용하여 사용자를 찾음
        return userRepository.findByLoginId(loginId);
    }

    // 닉네임으로 사용자 조회 메서드
    public Optional<User> findUserByNickname(String nickname) {
        // 닉네임을 사용하여 사용자를 찾음
        return userRepository.findByNickname(nickname);
    }

    // 이메일로 사용자 조회 메서드
    public Optional<User> findUserByEmail(String email) {
        // 이메일을 사용하여 사용자를 찾음
        return userRepository.findByEmail(email);
    }

    // userId를 제외한 모든 사용자 조회 메서드
    public List<User> findAllUsersExceptUser(Long userId) {
        // 모든 사용자를 조회하고 userId가 제외된 목록을 반환
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .filter(user -> !user.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}
