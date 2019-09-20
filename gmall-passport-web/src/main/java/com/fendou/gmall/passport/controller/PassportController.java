package com.fendou.gmall.passport.controller;

import com.fendou.gmall.bean.UmsMember;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * PassportController class
 *
 * @author maochaoying
 * @date 2019/9/19
 */
@Controller
public class PassportController {

    @RequestMapping("/index")
    public String index(HttpServletRequest request, ModelMap modelMap) {
        String returnUrl = request.getParameter("returnUrl");
        modelMap.put("returnUrl", returnUrl);
        return "index";
    }

    @RequestMapping("/login")
    @ResponseBody
    public String login(UmsMember umsMember) {


        //生成token返回
        return "token";
    }
}
