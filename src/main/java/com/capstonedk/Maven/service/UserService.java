package com.capstonedk.Maven.service;

import com.capstonedk.Maven.model.User;
import com.capstonedk.Maven.repository.UserRepository;
import com.capstonedk.Maven.model.request.UserCreationRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // User operations

    public User createUser(UserCreationRequest request) {
        User user = new User();
        BeanUtils.copyProperties(request, user);
        return userRepository.save(user);
    }

    public User updateUser(Long userId, UserCreationRequest request) {
        User user = findUser(userId);
        BeanUtils.copyProperties(request, user);
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EmptyResultDataAccessException("Cannot delete user with ID " + userId + " as it does not exist", 1);
        }
    }

    public User findUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new EmptyResultDataAccessException("User not present in the database", 1));
    }

    public Optional<User> loginUser(String loginId, String password) {
        return userRepository.findByLoginIdAndPassword(loginId, password);
    }

    public Optional<User> findUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }
}