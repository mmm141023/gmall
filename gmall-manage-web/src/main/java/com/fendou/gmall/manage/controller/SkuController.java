package com.fendou.gmall.manage.controller;

import com.fendou.gmall.bean.PmsProductImage;
import com.fendou.gmall.bean.PmsSkuInfo;
import com.fendou.gmall.service.SkuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SkuController class
 *
 * @author maochaoying
 * @date 2019/9/3
 */
@CrossOrigin
@RestController
public class SkuController {

    @Reference
    SkuService skuService;
    @RequestMapping("/saveSkuInfo")
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo) {
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
        String result = skuService.saveSkuInfo(pmsSkuInfo);
        return result;
    }

}
