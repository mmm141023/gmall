package com.fendou.gmall.service;

import com.fendou.gmall.bean.PmsSkuInfo;

/**
 * SkuService class
 *
 * @author maochaoying
 * @date 2019/9/3
 */

public interface SkuService {
    String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuListById(String skuId);
}
