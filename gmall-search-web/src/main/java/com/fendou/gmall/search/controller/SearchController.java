package com.fendou.gmall.search.controller;

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
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = searchService.list(pmsSearchParam);
        modelMap.put("skuLsInfoList", pmsSearchSkuInfoList);
        return "list";
    }
}
