package com.fendou.gmall.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * CartController class
 *
 * @author maochaoying
 * @date 2019/9/17
 */
@Controller
public class CartController {
    @RequestMapping("/addToCart")
    public String addToCart() {
        return "success";
    }
}
