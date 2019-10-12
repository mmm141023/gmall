package com.fendou.gmall.payment.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fendou.gmall.annotation.LoginRequired;
import com.fendou.gmall.payment.config.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * PaymentController class
 *
 * @author maochaoying
 * @date 2019/9/28
 */

@Controller
public class PaymentController {

    @Autowired
    AlipayClient alipayClient;


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

    @RequestMapping("/mx/submit")
    @LoginRequired(loginSuccess = true)
    public String mxSubmit(String outTradeNo, String totalAmount) {
        return "";
    }

    @RequestMapping("/alipay/submit")
    @LoginRequired(loginSuccess = true)
    @ResponseBody
    public String alipaySubmit(String outTradeNo, String totalAmount) {
        String form = null;
        AlipayTradePagePayRequest alipayTradePagePayRequest = new AlipayTradePagePayRequest();
        Map<String, Object> map = new HashMap<>();
        alipayTradePagePayRequest.setReturnUrl(AlipayConfig.return_payment_url);
        alipayTradePagePayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);
        map.put("out_trade_no", outTradeNo);
        map.put("product_code", "FAST_INSTANT_TRADE_PAY");
        map.put("total_amount", 0.01);
        map.put("subject", "毛毛奋斗者手机");
        String param = JSON.toJSONString(map);
        alipayTradePagePayRequest.setBizContent(param);
        try {
            form = alipayClient.pageExecute(alipayTradePagePayRequest).getBody();
            System.out.println(form);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }

    @RequestMapping("/alipay/callback/return")
    @LoginRequired(loginSuccess = true)
    public String AlipaycallBackReturn() {
        return "finish";
    }
}
