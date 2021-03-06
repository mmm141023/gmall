package com.fendou.gmall.manage.controller;

import com.fendou.gmall.bean.PmsBaseSaleAttr;
import com.fendou.gmall.bean.PmsProductImage;
import com.fendou.gmall.bean.PmsProductInfo;
import com.fendou.gmall.bean.PmsProductSaleAttr;
import com.fendou.gmall.manage.util.ImageUploadUtil;
import com.fendou.gmall.service.SpuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * SpuController class
 *
 * @author maochaoying
 * @date 2019/9/1
 */
@RestController
@CrossOrigin
public class SpuController {

    @Reference
    SpuService spuService;
    @RequestMapping("/spuList")
    public List<PmsProductInfo> spuList(String catalog3Id) {
        return spuService.spuList(catalog3Id);
    }

    @RequestMapping("/baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return spuService.baseSaleAttrList();
    }


    @RequestMapping("/fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile) {
        String url = ImageUploadUtil.imgUpload(multipartFile);
        return url;
    }
    @RequestMapping("/saveSpuInfo")
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {
        return spuService.saveSpuInfo(pmsProductInfo);
    }
    @RequestMapping("/spuSaleAttrList")
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
        return spuService.spuSaleAttrList(spuId);
    }
    @RequestMapping("/spuImageList")
    public List<PmsProductImage> spuImageList(String spuId) {
        return spuService.spuImageList(spuId);
    }


}

