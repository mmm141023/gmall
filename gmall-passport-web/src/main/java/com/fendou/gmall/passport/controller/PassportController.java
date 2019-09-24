package com.fendou.gmall.passport.controller;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.fendou.gmall.bean.UmsMember;
import com.fendou.gmall.service.UserService;
import com.fendou.gmall.util.HttpclientUtil;
import com.fendou.gmall.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
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

    @RequestMapping("/vlogin")
    public String vlogin(String code){

        Map<String,String> user_map = getWeiBoUserInfo(code);
        UmsMember umsMember = userService.saveWeiBoUserInfo(user_map);
        String salt = "dsfasdfsDAFsadsadDSDDCVeawSDSDsadsacw18182654891215";
        String key = "maochaoyingGmall";
        // 制作token
        String token = makeToken(salt, key, umsMember);
        return "redirect:http://localhost:8085/index?token=" + token;
    }

    private Map<String, String> getWeiBoUserInfo(String code) {
        String url = "https://api.weibo.com/oauth2/access_token";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id", "2357047914");
        paramMap.put("client_secret","f0e4a972fd27f9045ce72cfe9c17d4ad");
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("redirect_uri","http://127.0.0.1:8082/vlogin" );
        paramMap.put("code", code);
        String access_json = HttpclientUtil.doPost(url, paramMap);
        Map<String,String> access_map = JSON.parseObject(access_json, Map.class);
        String access_token = access_map.get("access_token");
        String uid = access_map.get("uid");
        // 通过access_token 和 uid 换取微博网站的信息
        String user_json = HttpclientUtil.doGet("https://api.weibo.com/2/users/show.json?access_token=" + access_token + "&uid=" + uid);
        Map<String,String> user_map = JSON.parseObject(user_json, Map.class);
        user_map.put("access_token", access_token);
        user_map.put("code", code);
        return user_map;
    }

    /**
     * App Key：2357047914
     * App Secret：f0e4a972fd27f9045ce72cfe9c17d4ad
     * 授权回调页：http://127.0.0.1:8082/vlogin
     * 取消授权回调页：http://127.0.0.1:8082/vlogout
     * https://api.weibo.com/oauth2/authorize?client_id=2357047914&response_type=code&redirect_uri=http://127.0.0.1:8082/vlogin
     * http://127.0.0.1:8082/vlogin?code=8aacde54b2e114c091ed8ee97edd2638
     * https://api.weibo.com/oauth2/access_token?client_id=2357047914&client_secret=f0e4a972fd27f9045ce72cfe9c17d4ad&grant_type=authorization_code&redirect_uri=http://127.0.0.1:8082/vlogin&code=8aacde54b2e114c091ed8ee97edd2638
     * @param args
     */
    // 测试微博接入平台
    public static void main(String[] args) {
        String url = "https://api.weibo.com/oauth2/access_token";
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("client_id", "2357047914");
        paramMap.put("client_secret","f0e4a972fd27f9045ce72cfe9c17d4ad");
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("redirect_uri","http://127.0.0.1:8082/vlogin" );
        paramMap.put("code", "8aacde54b2e114c091ed8ee97edd2638");
        String access_json = HttpclientUtil.doPost(url, paramMap);
        Map<String,String> map = JSON.parseObject(access_json, Map.class);
        String access_token = map.get("access_token");
        String uid = map.get("uid");
        System.out.println(access_token);
        System.out.println(uid);
    }
}
