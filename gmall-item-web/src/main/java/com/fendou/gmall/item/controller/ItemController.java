package com.fendou.gmall.item.controller;

import com.alibaba.fastjson.JSON;
import com.fendou.gmall.bean.PmsProductSaleAttr;
import com.fendou.gmall.bean.PmsSkuInfo;
import com.fendou.gmall.service.SkuService;
import com.fendou.gmall.service.SpuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * ItemController class
 *
 * @author maochaoying
 * @date 2019/9/4
 */
@Controller
public class ItemController {

    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;

    @RequestMapping("/{skuId}.html")
    public String item(@PathVariable String skuId , ModelMap modelMap) {
        PmsSkuInfo pmsSkuInfo = skuService.getSkuListById(skuId);
        String spuId = pmsSkuInfo.getProductId();
        Map<String, String> map = spuService.getAllSaleAttrListBySpuId(spuId);
        String saleAttrValueJsonStr = JSON.toJSONString(map);
        //映射关系 sku和saleattrvalueid
        modelMap.put("saleAttrValueJsonStr", saleAttrValueJsonStr);
        modelMap.put("skuInfo", pmsSkuInfo);
        List<PmsProductSaleAttr> pmsProductSaleAttrs = spuService.SelectSpuSaleAttrListCheckBySku(pmsSkuInfo.getProductId(),skuId);
        modelMap.put("spuSaleAttrListCheckBySku", pmsProductSaleAttrs);
        return "item";
    }


}
