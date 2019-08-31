package com.fendou.gmall.user.service.impl;

import com.fendou.gmall.bean.UmsMember;
import com.fendou.gmall.service.UserService;
import com.fendou.gmall.user.dao.UserMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * UserServiceImpl class
 *
 * @author maochaoying
 * @date 2019/8/30
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public String getAllUser() {
        return "helloworld";
    }
}
