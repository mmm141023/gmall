package com.fendou.gmall.cart.controller;

import com.alibaba.fastjson.JSON;
import com.fendou.gmall.bean.OmsCartItem;
import com.fendou.gmall.bean.PmsSkuInfo;
import com.fendou.gmall.service.SkuService;
import com.fendou.gmall.util.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CartController class
 *
 * @author maochaoying
 * @date 2019/9/17
 */
@Controller
public class CartController {

    @Reference
    SkuService skuService;

    @RequestMapping("/addToCart")
    public String addToCart(String skuId, BigDecimal quantity, ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        // 购物车商品信息可能不止一条，设置为集合
        List<OmsCartItem> omsCartItems = new ArrayList<>();

        // 从skuService中根据skuId查询info
        PmsSkuInfo skuListById = skuService.getSkuListById(skuId);
        // 将skuInfo封装为购物车对象
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setId("");
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

        //设置memberId 来代表用户是否登录
        String memberId = "";

        if (StringUtils.isBlank(memberId)) {
            // 用户未登录 购物车存入cookie
            String cartListCookie = CookieUtils.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)) {
                // 说明该cookie已经存在
                List<OmsCartItem> omsCartItems1 = JSON.parseArray(cartListCookie, OmsCartItem.class);
                for (OmsCartItem cartItem : omsCartItems1) {
                    omsCartItems.add(cartItem);
                    if (omsCartItem.getProductSkuId().equals(cartItem.getProductSkuId())) {
                        omsCartItems.remove(cartItem);
                    }
                }
                omsCartItems.add(omsCartItem);
                // 将购物车对象转化为String
                String jsonString = JSON.toJSONString(omsCartItems);
                CookieUtils.setCookie(request, response, "cartListCookie", jsonString, 60 * 60 * 72, true);
            }else {
                // 该cookie不存在
                omsCartItems.add(omsCartItem);
                String jsonString = JSON.toJSONString(omsCartItems);
                CookieUtils.setCookie(request, response, "cartListCookie", jsonString, 60 * 60 * 72, true);
            }
        }else {
            // 用户已登录 购物车存入db
        }
        modelMap.put("skuInfo", skuListById);
        modelMap.put("skuNum", quantity);
        return "success";
    }

    @RequestMapping("/cartList")
    public String cartList() {


        return "cartList";
    }
}
