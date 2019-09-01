package com.fendou.gmall.manage.controller;

import com.fendou.gmall.bean.PmsProductInfo;
import com.fendou.gmall.service.SpuService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

