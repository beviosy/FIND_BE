package com.capstonedk.Maven.controller;

import com.capstonedk.Maven.model.Review;
import com.capstonedk.Maven.model.Store;
import com.capstonedk.Maven.model.request.StoreCreationRequest;
import com.capstonedk.Maven.model.response.ApiResponse;
import com.capstonedk.Maven.model.response.StoreInfo;
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
    public ResponseEntity<ApiResponse> findByCategoryId(@PathVariable int categoryId) {
        try {
            List<StoreInfo> storeInfos;
            if (categoryId == 0) {
                List<Store> stores = storeService.readStores();
                storeInfos = stores.stream()
                        .map(store -> new StoreInfo(store.getStorePictureUrl(), store.getStoreName(), store.getInfo()))
                        .collect(Collectors.toList());
            } else {
                List<Store> stores = storeService.getStoresByCategoryId(categoryId);
                storeInfos = stores.stream()
                        .map(store -> new StoreInfo(store.getStorePictureUrl(), store.getStoreName(), store.getInfo()))
                        .collect(Collectors.toList());
            }
            return ResponseEntity.ok(new ApiResponse(true, "STORE_LIST_FOUND", "가게 리스트 조회 성공", storeInfos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "INTERNAL_SERVER_ERROR", "가게 리스트 조회 중 오류가 발생했습니다.", null));
        }
    }

    @Operation(summary = "store ID로 맛집 찾기", description = "맛집 상세정보 제공")
    @GetMapping("/storelist/{storeId}")
    public ResponseEntity<ApiResponse> findStore(@PathVariable Long storeId) {
        try {
            Store store = storeService.findStore(storeId);
            return ResponseEntity.ok(new ApiResponse(true, "STORE_FOUND", "가게 정보 조회 성공", store));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "STORE_NOT_FOUND", "가게를 찾을 수 없습니다.", null));
        }
    }

    @Operation(summary = "새로운 맛집 등록", description = "새로운 맛집 정보 등록")
    @PostMapping("/storelist")
    public ResponseEntity<ApiResponse> createStore(
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
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "STORE_CREATED", "가게가 성공적으로 생성되었습니다.", createdStore));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "INTERNAL_SERVER_ERROR", "가게 생성 중 오류가 발생했습니다.", null));
        }
    }

    @Operation(summary = "맛집 정보 수정", description = "기존 맛집 정보 수정")
    @PutMapping("/storelist/{storeId}")
    public ResponseEntity<ApiResponse> updateStore(@PathVariable Long storeId, @RequestBody StoreCreationRequest request) {
        try {
            Store updatedStore = storeService.updateStore(storeId, request);
            return ResponseEntity.ok(new ApiResponse(true, "STORE_UPDATED", "가게 정보가 성공적으로 수정되었습니다.", updatedStore));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "INTERNAL_SERVER_ERROR", "가게 정보 수정 중 오류가 발생했습니다.", null));
        }
    }

    @Operation(summary = "맛집 삭제", description = "특정 맛집 삭제")
    @DeleteMapping("/storelist/{storeId}")
    public ResponseEntity<ApiResponse> deleteStore(@PathVariable Long storeId) {
        try {
            storeService.deleteStore(storeId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "INTERNAL_SERVER_ERROR", "가게 삭제 중 오류가 발생했습니다.", null));
        }
    }

    @Operation(summary = "가게 리뷰 조회", description = "특정 가게의 리뷰를 조회합니다.")
    @GetMapping("/storelist/{storeId}/reviews")
    public ResponseEntity<ApiResponse> getStoreReviews(@PathVariable Long storeId) {
        try {
            List<Review> reviews = reviewService.findReviewsByStoreId(storeId);
            return ResponseEntity.ok(new ApiResponse(true, "REVIEWS_FOUND", "리뷰 조회 성공", reviews));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "INTERNAL_SERVER_ERROR", "리뷰 조회 중 오류가 발생했습니다.", null));
        }
    }
}
