package com.capstonedk.Maven.controller;

import com.capstonedk.Maven.model.request.StoreCreationRequest;
import com.capstonedk.Maven.model.Store;
import com.capstonedk.Maven.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/storelistAll")
    public List<Store> storelistAll()
    {
        return storeService.readStores();
    }//전체 리스트

    @GetMapping("/storelist/category/{categoryId}")
    public ResponseEntity<List<Store>> findByCategoryId(@PathVariable int categoryId) {
        List<Store> stores = storeService.getStoresByCategoryId(categoryId);
        return ResponseEntity.ok(stores);
    }//카데고리로 조회

    @GetMapping("/storelist/{storeId}")
    public ResponseEntity<Store> findStore(@PathVariable Long storeId) {
        Store store = storeService.findStore(storeId);
        return ResponseEntity.ok(store);
    }//맛집 아이디로 조회

    @PostMapping("/storelist/{storeId}")
    public ResponseEntity<Store> createStore(@RequestBody StoreCreationRequest request) {
        Store createdStore = storeService.createStore(request);
        return ResponseEntity.ok(createdStore);
    }//맛집 생성

    @PatchMapping("/storelist/{storeId}")
    public ResponseEntity<Store> updateStore(@RequestBody StoreCreationRequest request, @PathVariable Long storeId) {
        Store updatedStore = storeService.updateStore(storeId, request);
        return ResponseEntity.ok(updatedStore);
    }//맛집 수정

    @DeleteMapping("/storelist/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.ok().build();
    }
}//맛집 삭제

