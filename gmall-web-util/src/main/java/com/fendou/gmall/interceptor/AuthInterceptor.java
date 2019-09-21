package com.fendou.gmall.interceptor;

import com.fendou.gmall.annotation.LoginRequired;
import com.fendou.gmall.util.CookieUtils;
import com.fendou.gmall.util.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if (methodAnnotation == null) {
            return true;
        }
        boolean loginResult = methodAnnotation.loginSuccess();
        // 如果newToken 和 oldToken都有值说明 oldToken 过期
        String token = "";
        String oldToken = CookieUtils.getCookieValue(request, "oldToken", true);
        if (StringUtils.isNotBlank(oldToken)) {
            token = oldToken;
        }
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(newToken)) {
            token = newToken;
        }
        String verifyResult = "failed";
        //验证token
        if (StringUtils.isNotBlank(token)) {
            verifyResult = HttpclientUtil.doGet("http://localhost:8082/verify?token=" + token);
            CookieUtils.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
        }
        if (loginResult) {
            //必须登录
            if (verifyResult.equals("success")) {
                // 取出token信息
                request.setAttribute("name", "nickname");
                request.setAttribute("memberId", "1");
                return true;
            }else {
                //重定向回passport页面登录
                // 重定向回原来的界面
                StringBuffer requestURL = request.getRequestURL();
                String url = requestURL.toString();
                response.sendRedirect("http://localhost:8082/index?returnUrl=" + url);
                return false;
            }
        }else {
            // 不是必须登录
            // 如果登录
            if (verifyResult.equals("success")) {
                // 取出token信息
                request.setAttribute("name", "nickname");
                request.setAttribute("memberId", "1");
            }
        }
        return true;
    }
}