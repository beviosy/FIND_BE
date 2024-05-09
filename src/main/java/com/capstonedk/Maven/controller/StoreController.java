package com.capstonedk.Maven.controller;

import com.capstonedk.Maven.model.Store;
import com.capstonedk.Maven.model.request.StoreCreationRequest;
import com.capstonedk.Maven.service.StoreService;
import com.capstonedk.Maven.model.response.StoreInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/store")
@Tag(name = "Store", description = "맛집 API")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @Operation(summary = "category ID로 맛집 찾기", description = "0: 전체, 1: 한식, 2: 중식, 3: 양식, 4: 일식")
    @GetMapping("/storelist/category/{categoryId}")
    public ResponseEntity<List<StoreInfo>> findByCategoryId(@PathVariable int categoryId) {
        List<Store> stores;
        if (categoryId == 0) {
            stores = storeService.readStores();
        } else {
            stores = storeService.getStoresByCategoryId(categoryId);
        }
        List<StoreInfo> storeInfos = new ArrayList<>();
        for (Store store : stores) {
            storeInfos.add(new StoreInfo(store.getStoreName(), store.getInfo()));
        }
        return ResponseEntity.ok(storeInfos);
    }

    @Operation(summary = "store ID로 맛집 찾기", description = "맛집 상세정보 제공")
    @GetMapping("/storelist/{storeId}")
    public ResponseEntity<Store> findStore(@PathVariable Long storeId) {
        try {
            Store store = storeService.findStore(storeId);
            return ResponseEntity.ok(store);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "새로운 맛집 등록", description = "새로운 맛집 정보 등록")
    @PostMapping("/storelist")
    public ResponseEntity<Store> createStore(@RequestBody StoreCreationRequest request) {
        try {
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
