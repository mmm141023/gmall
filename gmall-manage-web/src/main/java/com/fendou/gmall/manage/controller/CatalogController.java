package com.fendou.gmall.manage.controller;

import com.fendou.gmall.bean.PmsBaseCatalog1;
import com.fendou.gmall.bean.PmsBaseCatalog2;
import com.fendou.gmall.bean.PmsBaseCatalog3;
import com.fendou.gmall.service.CatalogService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
public class CatalogController {
    @Reference
    CatalogService catalogService;

    /**
     * 查询一级列表
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCatalog1")
    public List<PmsBaseCatalog1> getCatalog1() {
        return catalogService.getCatalog1();
    }

    /**
     * 根据一级列表ID查询二级列表
     * @param catalog1Id
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCatalog2")
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id) {
        return catalogService.getCatalog2(catalog1Id);
    }

    /**
     * 根据二级列表ID查询三级列表
     * @param catalog2Id
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCatalog3")
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        return catalogService.getCatalog3(catalog2Id);
    }



}
