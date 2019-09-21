package com.fendou.gmall.passport.controller;

import com.fendou.gmall.bean.UmsMember;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
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
    /**
     * 登录首页
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, ModelMap modelMap) {
        String returnUrl = request.getParameter("returnUrl");
        modelMap.put("returnUrl", returnUrl);
        return "index";
    }

    /**
     * 登录按钮请求  参数为账号和密码
     * @param umsMember
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public String login(UmsMember umsMember) {


        //生成token返回
        return "token";
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verify(HttpServletRequest request) {
        String token = request.getParameter("token");

        return "success";
    }
}
