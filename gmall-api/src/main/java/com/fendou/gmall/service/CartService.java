package com.fendou.gmall.service;

import com.fendou.gmall.bean.OmsCartItem;
import com.fendou.gmall.bean.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * CartService class
 *
 * @author maochaoying
 * @date 2019/9/18
 */

public interface CartService {
    OmsCartItem transSkuInfoToCartItem(PmsSkuInfo skuListById, BigDecimal quantity);

    OmsCartItem transSkuInfoToCartItemWhenLogin(PmsSkuInfo skuListById, BigDecimal quantity, String memberId);

    String saveOmsCartItem(OmsCartItem omsCartItem, String skuId, String memberId);

    String saveOmsCartItemFromCache(List<OmsCartItem> omsCartItems1, String skuId, String memberId);

    void flushCartCache(String memberId);
}
