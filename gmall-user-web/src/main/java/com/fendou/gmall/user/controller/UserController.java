package com.fendou.gmall.user.controller;

import com.fendou.gmall.bean.UmsMember;
import com.fendou.gmall.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * UserController class
 *
 * @author maochaoying
 * @date 2019/8/31
 */
@Controller
public class UserController {
    @Reference
    UserService userService;

    @ResponseBody
    @RequestMapping("/getAll")
    public List<UmsMember> helloWorld() {
        return userService.getAllUser();
    }
    @ResponseBody
    @RequestMapping("/index")
    public List<UmsMember> getAllUser() {
        return userService.getAllUser2();
    }
}
