package com.fendou.gmall.order.controller;

import com.fendou.gmall.annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * OrderController class
 *
 * @author maochaoying
 * @date 2019/9/24
 */

@Controller
public class OrderController {

    @RequestMapping("/toTrade")
    @LoginRequired(loginSuccess = true)
    public String toTrade(HttpServletRequest request) {

        String memberId =(String) request.getAttribute("memberId");
        String nickname =(String) request.getAttribute("nickname");


        return "trade";
    }
}
