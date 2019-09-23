package com.fendou.gmall.passport.controller;

import com.alibaba.fastjson.JSON;
import com.fendou.gmall.bean.UmsMember;
import com.fendou.gmall.service.UserService;
import com.fendou.gmall.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * PassportController class
 *
 * @author maochaoying
 * @date 2019/9/19
 */
@Controller
public class PassportController {

    @Reference
    UserService userService;

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
    public String login(UmsMember umsMember,HttpServletRequest request) {
        String salt = "dsfasdfsDAFsadsadDSDDCVeawSDSDsadsacw18182654891215";
        String key = "maochaoyingGmall";
        String token = "";
        UmsMember umsMemberOne = userService.queryFromCache(umsMember);
        if (umsMemberOne == null) {
            //说明缓存中没有数据  需要从数据库中查询
            umsMemberOne = userService.queryUmsMemberOne(umsMember);
            if(umsMemberOne != null) {
                //用户名密码正确
                // 制作token
                token = makeToken(salt, key,umsMemberOne);
                // 将用户信息存入缓存
                userService.saveUserInfoCache(umsMemberOne);
            }else{
                // 用户名密码错误
                return "failed";
            }
        }else{
            // 制作token
            token = makeToken(salt, key,umsMemberOne);
        }
        return token;
    }

    private String makeToken(String salt, String key, UmsMember umsMemberOne) {
        Map<String,Object> param = new HashMap<>();
        param.put("memberId", umsMemberOne.getId());
        param.put("nickname", umsMemberOne.getNickname());
        String token = JwtUtil.encode(key, param, salt);
        return token;
    }


    @RequestMapping("/verify")
    @ResponseBody
    public String verify(HttpServletRequest request) {
        Map<String,String> map = new HashMap<>();
        String token = request.getParameter("token");
        String salt = "dsfasdfsDAFsadsadDSDDCVeawSDSDsadsacw18182654891215";
        String key = "maochaoyingGmall";
        Map<String, Object> decode = JwtUtil.decode(token, key, salt);
        if (decode == null) {
            map.put("status", "failed");
        }else{
            String memberId = (String) decode.get("memberId");
            String nickname = (String) decode.get("nickname");
            map.put("status", "success");
            map.put("memberId", memberId);
            map.put("nickname", nickname);
        }
        return JSON.toJSONString(map);
    }
}
