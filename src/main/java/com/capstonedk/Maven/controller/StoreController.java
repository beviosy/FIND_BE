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

    @GetMapping("/storelist")
    public List<Store> getStoreList()
    {
        return storeService.readStores();
    }//전체 리스트

    @GetMapping("/storelist{categoryId}")
    public ResponseEntity<Store> CategoryStore(@PathVariable int categoryId) {
        Store store = storeService.getCategoryStore(categoryId);
        return ResponseEntity.ok(store);
    }

    @GetMapping("/storelist/{storeId}")
    public ResponseEntity<Store> findStore(@PathVariable Long storeId) {
        Store store = storeService.findStore(storeId);
        return ResponseEntity.ok(store);
    }

    @PostMapping("/storelist")
    public ResponseEntity<Store> createStore(@RequestBody StoreCreationRequest request) {
        Store createdStore = storeService.createStore(request);
        return ResponseEntity.ok(createdStore);
    }

    @PatchMapping("/storelist/{storeId}")
    public ResponseEntity<Store> updateStore(@RequestBody StoreCreationRequest request, @PathVariable Long storeId) {
        Store updatedStore = storeService.updateStore(storeId, request);
        return ResponseEntity.ok(updatedStore);
    }

    @DeleteMapping("/storelist/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId) {
        storeService.deleteStore(storeId);
        return ResponseEntity.ok().build();
    }
}

