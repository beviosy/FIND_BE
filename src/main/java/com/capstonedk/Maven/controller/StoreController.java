package com.capstonedk.Maven.controller;

import com.capstonedk.Maven.model.Store;
import com.capstonedk.Maven.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/store")
@Tag(name = "store", description = "맛집 API")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @Operation(summary = "category ID로 맛집 찾기", description = "0: 전체, 1: 한식, 2: 중식, 3: 양식, 4: 일식")
    @GetMapping("/storelist/category/{categoryId}")
    public ResponseEntity<List<Store>> findByCategoryId(@PathVariable int categoryId) {
        if (categoryId == 0) {
            List<Store> stores = storeService.readStores();
            return ResponseEntity.ok(stores);
        } else {
            List<Store> stores = storeService.getStoresByCategoryId(categoryId);
            return ResponseEntity.ok(stores);
        }
    }

    @Operation(summary = "store ID로 맛집 찾기", description = "맛집 상세정보 제공")
    @GetMapping("/storelist/{storeId}")
    public ResponseEntity<Store> findStore(@PathVariable Long storeId) {
        Store store = storeService.findStore(storeId);
        return ResponseEntity.ok(store);
    }
}
