package com.fendou.gmall.cart.service.impl;

import com.fendou.gmall.bean.OmsCartItem;
import com.fendou.gmall.bean.PmsSkuInfo;
import com.fendou.gmall.cart.service.dao.OmsCartItemMapper;
import com.fendou.gmall.service.CartService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * CartServiceImpl class
 *
 * @author maochaoying
 * @date 2019/9/18
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    OmsCartItemMapper omsCartItemMapper;

    @Override
    public OmsCartItem transSkuInfoToCartItem(PmsSkuInfo skuListById, BigDecimal quantity) {
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setProductId(skuListById.getProductId());
        omsCartItem.setProductSkuId(skuListById.getId());
        omsCartItem.setQuantity(quantity);
        omsCartItem.setPrice(skuListById.getPrice());
        omsCartItem.setSp1("颜色");
        omsCartItem.setSp2("重量");
        omsCartItem.setSp3("容量");
        omsCartItem.setProductPic(skuListById.getSkuDefaultImg());
        omsCartItem.setProductName(skuListById.getSkuName());
        omsCartItem.setProductSkuCode("123123123123123123123");
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setModifyDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setProductCategoryId(skuListById.getCatalog3Id());
        omsCartItem.setIsChecked("1");
        omsCartItem.setTotalPrice(quantity.multiply(omsCartItem.getPrice()));
        return omsCartItem;
    }

    @Override
    public OmsCartItem transSkuInfoToCartItemWhenLogin(PmsSkuInfo skuListById, BigDecimal quantity, String memberId) {
        OmsCartItem omsCartItem = transSkuInfoToCartItem(skuListById, quantity);
        omsCartItem.setMemberId(memberId);
        omsCartItem.setMemberNickname("maomao");
        return omsCartItem;
    }

    @Override
    public String saveOmsCartItem(OmsCartItem omsCartItem, String skuId, String memberId) {

        OmsCartItem omsCartItem1 = new OmsCartItem();
        omsCartItem1.setMemberId(memberId);
        omsCartItem1.setProductSkuId(skuId);
        OmsCartItem omsCartItem2 = omsCartItemMapper.selectOne(omsCartItem1);
        if (omsCartItem2 == null) {
            omsCartItem.setMemberId(memberId);
            omsCartItem.setMemberNickname("maomao");
            omsCartItemMapper.insertSelective(omsCartItem);
        }else {
            Example example = new Example(OmsCartItem.class);
            example.createCriteria().andEqualTo("productSkuId", skuId).andEqualTo("memberId", memberId);
            omsCartItemMapper.updateByExampleSelective(omsCartItem, example);
        }
        return "success";
    }

    @Override
    public String saveOmsCartItemFromCache(List<OmsCartItem> omsCartItems1, String skuId, String memberId) {
        String result = "";
        if (omsCartItems1 == null) {
            result = "failed";
            return result;
        }else {
            for (OmsCartItem omsCartItem : omsCartItems1) {
                String productSkuId = omsCartItem.getProductSkuId();
                result = saveOmsCartItem(omsCartItem, productSkuId, memberId);
            }
        }
        return result;
    }
}
