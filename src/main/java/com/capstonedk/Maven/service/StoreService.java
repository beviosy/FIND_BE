package com.capstonedk.Maven.service;

import com.capstonedk.Maven.model.Store;
import com.capstonedk.Maven.model.User;
import com.capstonedk.Maven.model.Review;
import com.capstonedk.Maven.repository.StoreRepository;
import com.capstonedk.Maven.repository.UserRepository;
import com.capstonedk.Maven.repository.ReviewRepository;
import com.capstonedk.Maven.model.request.StoreCreationRequest;
import com.capstonedk.Maven.model.request.UserCreationRequest;
import com.capstonedk.Maven.model.request.ReviewCreationRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;

    // Store operations

    public Store findStore(Long storeId) {
        Optional<Store> store = storeRepository.findById(storeId);
        return store.orElseThrow(() -> new EmptyResultDataAccessException("Cannot find any store under the given ID", 1));
    }

    public List<Store> readStores() {
        return storeRepository.findAll();
    }

    public Store getCategoryStore(int categoryId) {
        Optional<Store> store = storeRepository.findByCategoryId(categoryId);
        return store.orElseThrow(() -> new EmptyResultDataAccessException("Cannot find any store under the given category ID", 1));
    }

    public Store createStore(StoreCreationRequest request) {
        Store store = new Store();
        BeanUtils.copyProperties(request, store);
        return storeRepository.save(store);
    }

    public Store updateStore(Long storeId, StoreCreationRequest request) {
        Store store = findStore(storeId);
        BeanUtils.copyProperties(request, store);
        return storeRepository.save(store);
    }

    public void deleteStore(Long storeId) {
        try {
            storeRepository.deleteById(storeId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EmptyResultDataAccessException("Cannot delete store with ID " + storeId + " as it does not exist", 1);
        }
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

    private User findUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new EmptyResultDataAccessException("User not present in the database", 1));
    }

    // Review operations

    public Review createReview(ReviewCreationRequest request) {
        Review review = new Review();
        BeanUtils.copyProperties(request, review);
        return reviewRepository.save(review);
    }

    public Review updateReview(Long reviewId, ReviewCreationRequest request) {
        Review review = findReview(reviewId);
        BeanUtils.copyProperties(request, review);
        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        try {
            reviewRepository.deleteById(reviewId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EmptyResultDataAccessException("Cannot delete review with ID " + reviewId + " as it does not exist", 1);
        }
    }

    private Review findReview(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        return review.orElseThrow(() -> new EmptyResultDataAccessException("Review not present in the database", 1));
    }
}

