package com.fendou.gmall.passport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * PassportController class
 *
 * @author maochaoying
 * @date 2019/9/19
 */
@Controller
public class PassportController {

    @RequestMapping("/index")
    public String index() {

        return "index";
    }
}
