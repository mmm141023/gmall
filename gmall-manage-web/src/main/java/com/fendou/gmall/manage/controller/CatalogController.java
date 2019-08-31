package com.fendou.gmall.manage.controller;

import com.fendou.gmall.bean.PmsBaseCatalog1;
import com.fendou.gmall.service.CatalogService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * CatalogController class
 *
 * @author maochaoying
 * @date 2019/8/31
 */
@Controller
public class CatalogController {
    @Reference
    CatalogService catalogService;
    @ResponseBody
    @RequestMapping("/getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1() {
        return catalogService.getCatalog1();
    }

}
