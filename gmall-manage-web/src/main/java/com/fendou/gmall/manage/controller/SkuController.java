package com.fendou.gmall.manage.controller;

import com.fendou.gmall.bean.PmsProductImage;
import com.fendou.gmall.bean.PmsSkuInfo;
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

    @RequestMapping("/saveSkuInfo")
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo) {
        pmsSkuInfo.setProductId(pmsSkuInfo.getSpuId());
        return "success";
    }

}
