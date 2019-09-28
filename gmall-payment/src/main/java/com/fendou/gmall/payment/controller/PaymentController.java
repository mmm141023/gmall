package com.fendou.gmall.payment.controller;

import com.fendou.gmall.annotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * PaymentController class
 *
 * @author maochaoying
 * @date 2019/9/28
 */

@Controller
public class PaymentController {

    @RequestMapping("/index")
    @LoginRequired(loginSuccess = true)
    public String index(HttpServletRequest request, ModelMap modelMap) {

        String nickname = (String) request.getAttribute("nickname");

        String orderSn = request.getParameter("orderSn");
        String totalAmount = request.getParameter("totalAmount");

        modelMap.put("nickName", nickname);
        modelMap.put("totalAmount", totalAmount);
        modelMap.put("orderId", orderSn);

        return "index";
    }
}
