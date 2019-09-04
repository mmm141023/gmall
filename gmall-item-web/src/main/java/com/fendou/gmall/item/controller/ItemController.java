package com.fendou.gmall.item.controller;

import com.fendou.gmall.bean.PmsSkuInfo;
import com.fendou.gmall.service.SkuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping("/{skuId}.html")
    public String item(@PathVariable String skuId , ModelMap modelMap) {
        PmsSkuInfo pmsSkuInfo = skuService.getSkuListById(skuId);
        modelMap.put("skuInfo", pmsSkuInfo);


        return "item";
    }
}
