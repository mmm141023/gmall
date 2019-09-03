package com.fendou.gmall.service;

import com.fendou.gmall.bean.PmsBaseSaleAttr;
import com.fendou.gmall.bean.PmsProductImage;
import com.fendou.gmall.bean.PmsProductInfo;
import com.fendou.gmall.bean.PmsProductSaleAttr;

import java.util.List;

/**
 * SpuService class
 *
 * @author maochaoying
 * @date 2019/9/1
 */

public interface SpuService {
    List<PmsProductInfo> spuList(String catalog3Id);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    String saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);

    List<PmsProductImage> spuImageList(String spuId);
}
