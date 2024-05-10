package com.capstonedk.Maven.service;

import com.capstonedk.Maven.model.Store;
import com.capstonedk.Maven.repository.StoreRepository;
import com.capstonedk.Maven.model.request.StoreCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    public Store findStore(Long storeId) {
        Optional<Store> store = storeRepository.findById(storeId);
        return store.orElseThrow(() -> new EmptyResultDataAccessException("Cannot find any store under the given ID", 1));
    }

    public List<Store> readStores() {
        return storeRepository.findAll();
    }

    public List<Store> getStoresByCategoryId(int categoryId) {
        return storeRepository.findByCategoryId(categoryId);
    }

    public Store createStore(StoreCreationRequest request) {
        Store store = new Store();
        BeanUtils.copyProperties(request, store);
        store.setRatingAverage((float)0.0);
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
}
