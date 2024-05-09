package com.capstonedk.Maven.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreInfo {
    private String storeName;
    private String info;
}//이름과 간단 정보만 나오게 수정"