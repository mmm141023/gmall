package com.fendou.gmall.user.service.impl;

import com.fendou.gmall.UserService;
import org.apache.dubbo.config.annotation.Service;

/**
 * UserServiceImpl class
 *
 * @author maochaoying
 * @date 2019/8/30
 */

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String getAllUser() {
        return "hello world";
    }
}
