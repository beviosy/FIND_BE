package com.capstonedk.Maven.model.response;

import com.capstonedk.Maven.model.Store;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class StoreInfo {
    private Long storeId;
    private String storePictureUrl;
    private String storeName;
    private String info;

    public static StoreInfo fromStore(Store store) {
        return new StoreInfo(store.getStoreId(),store.getStorePictureUrl(), store.getStoreName(), store.getInfo());
    }

    public static List<StoreInfo> fromStores(List<Store> stores) {
        List<StoreInfo> storeInfos = new ArrayList<>();
        for (Store store : stores) {
            storeInfos.add(fromStore(store));
        }
        return storeInfos;
    }
}