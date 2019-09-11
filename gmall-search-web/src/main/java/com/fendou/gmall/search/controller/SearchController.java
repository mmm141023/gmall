package com.fendou.gmall.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * SearchController class
 *
 * @author maochaoying
 * @date 2019/9/11
 */
@Controller
public class SearchController {
    @RequestMapping("/index")
    public String index() {
        return "index";
    }
    @RequestMapping("list.html")
    public String list(String catalog3Id) {
        return "list";
    }
}
