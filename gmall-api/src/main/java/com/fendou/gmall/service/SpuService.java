package com.fendou.gmall.service;

import com.fendou.gmall.bean.PmsProductInfo;

import java.util.List;

/**
 * SpuService class
 *
 * @author maochaoying
 * @date 2019/9/1
 */

public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);
}
