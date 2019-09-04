package com.fendou.gmall.item.controller;

import org.springframework.stereotype.Controller;
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
    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
