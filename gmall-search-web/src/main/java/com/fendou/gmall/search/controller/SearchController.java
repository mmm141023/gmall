package com.fendou.gmall.search.controller;

import com.fendou.gmall.bean.PmsBaseAttrInfo;
import com.fendou.gmall.bean.PmsSearchParam;
import com.fendou.gmall.bean.PmsSearchSkuInfo;
import com.fendou.gmall.service.SearchService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * SearchController class
 *
 * @author maochaoying
 * @date 2019/9/11
 */
@Controller
public class SearchController {

    @Reference
    SearchService searchService;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
    @RequestMapping("list.html")
    public String list(PmsSearchParam pmsSearchParam, ModelMap modelMap) {
        // list获得商品列表
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = searchService.list(pmsSearchParam);
        // getAttrValueAndAttrValueList获得筛选列表
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = searchService.getAttrValueAndAttrValueList(pmsSearchSkuInfoList);
        // 得到urlParam
        String urlParam = searchService.getUrlParam(pmsSearchParam);
        // 去除点击过的属性
        pmsBaseAttrInfos = searchService.delClickedAttrValue(pmsSearchParam,pmsBaseAttrInfos);
        // 返回前台
        modelMap.put("attrList", pmsBaseAttrInfos);
        modelMap.put("urlParam", urlParam);
        modelMap.put("skuLsInfoList", pmsSearchSkuInfoList);
        return "list";
    }
}
