package com.capstonedk.Maven.controller;

import com.capstonedk.Maven.model.Review;
import com.capstonedk.Maven.model.Store;
import com.capstonedk.Maven.model.request.StoreCreationRequest;
import com.capstonedk.Maven.model.response.StoreInfo;
import com.capstonedk.Maven.model.response.StoreWithReviewsResponse; // 추가
import com.capstonedk.Maven.service.ReviewService;
import com.capstonedk.Maven.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/store")
@Tag(name = "store", description = "맛집 API")
public class StoreController {

    private final StoreService storeService;
    private final ReviewService reviewService;

    @Autowired
    public StoreController(StoreService storeService, ReviewService reviewService) {
        this.storeService = storeService;
        this.reviewService = reviewService;
    }

    @Operation(summary = "category ID로 맛집 찾기", description = "0: 전체, 1: 한식, 2: 중식, 3: 양식, 4: 일식")
    @GetMapping("/storelist/category/{categoryId}")
    public ResponseEntity<List<StoreInfo>> findByCategoryId(@PathVariable int categoryId) {
        List<StoreInfo> storeInfos;
        if (categoryId == 0) {
            // 전체 가게 정보를 가져와서 StoreInfo로 변환
            List<Store> stores = storeService.readStores();
            storeInfos = stores.stream()
                    .map(store -> new StoreInfo(store.getStorePictureUrl(), store.getStoreName(), store.getInfo()))
                    .collect(Collectors.toList());
        } else {
            // 해당 카테고리에 속하는 가게 정보를 가져와서 StoreInfo로 변환
            List<Store> stores = storeService.getStoresByCategoryId(categoryId);
            storeInfos = stores.stream()
                    .map(store -> new StoreInfo(store.getStorePictureUrl(), store.getStoreName(), store.getInfo()))
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(storeInfos);
    }

    @Operation(summary = "store ID로 맛집 상세정보와 리뷰 목록 찾기", description = "맛집 상세정보 및 리뷰 목록 제공")
    @GetMapping("/storelist/{storeId}")
    public ResponseEntity<StoreWithReviewsResponse> findStore(@PathVariable Long storeId) {
        try {
            Store store = storeService.findStore(storeId);
            List<Review> reviews = reviewService.findReviewsByStoreId(storeId);
            StoreWithReviewsResponse response = new StoreWithReviewsResponse(store, reviews);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "새로운 맛집 등록", description = "새로운 맛집 정보 등록")
    @PostMapping("/storelist")
    public ResponseEntity<Store> createStore(
            @RequestParam String storePictureUrl,
            @RequestParam String storeName,
            @RequestParam String storeAddress,
            @RequestParam String storePhoneNumber,
            @RequestParam float latitude,
            @RequestParam float longitude,
            @RequestParam int categoryId,
            @RequestParam String info) {
        try {
            StoreCreationRequest request = new StoreCreationRequest();
            request.setStorePictureUrl(storePictureUrl);
            request.setStoreName(storeName);
            request.setStoreAddress(storeAddress);
            request.setStorePhoneNumber(storePhoneNumber);
            request.setLatitude(latitude);
            request.setLongitude(longitude);
            request.setCategoryId(categoryId);
            request.setInfo(info);
            Store createdStore = storeService.createStore(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStore);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "맛집 정보 수정", description = "기존 맛집 정보 수정")
    @PutMapping("/storelist/{storeId}")
    public ResponseEntity<Store> updateStore(@PathVariable Long storeId, @RequestBody StoreCreationRequest request) {
        try {
            Store updatedStore = storeService.updateStore(storeId, request);
            return ResponseEntity.ok(updatedStore);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "맛집 삭제", description = "특정 맛집 삭제")
    @DeleteMapping("/storelist/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId) {
        try {
            storeService.deleteStore(storeId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
