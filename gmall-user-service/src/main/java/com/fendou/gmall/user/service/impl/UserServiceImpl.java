package com.fendou.gmall.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.fendou.gmall.bean.UmsMember;
import com.fendou.gmall.service.UserService;
import com.fendou.gmall.user.dao.UserMapper;
import com.fendou.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

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
    @Autowired
    RedisUtil redisUtil;
    /**
     * 查询数据库是否存在umsMember
     * @param umsMember
     * @return
     */
    @Override
    public UmsMember queryUmsMemberOne(UmsMember umsMember) {
        UmsMember umsMember1 = userMapper.selectOne(umsMember);
        return umsMember1;
    }

    /**
     * 将用户信息存入redis
     * @param umsMemberOne
     */
    @Override
    public void saveUserInfoCache(UmsMember umsMemberOne) {
        String username = umsMemberOne.getUsername();
        String password = umsMemberOne.getPassword();
        Jedis jedis = null;
        try{
            jedis = redisUtil.getJedis();
            jedis.setex("user:" + username + ":password", 60*60*60*24, password);
            jedis.setex("user:" + password + ":info", 60*60*60*24, JSON.toJSONString(umsMemberOne));
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }

    /**
     * 从缓存中查询用户
     * @param umsMember
     * @return
     */
    @Override
    public UmsMember queryFromCache(UmsMember umsMember) {
        String username = umsMember.getUsername();
        String password = umsMember.getPassword();
        UmsMember umsMember1 = null;
        Jedis jedis = null;
        try{
            jedis = redisUtil.getJedis();
            String passwordCache = jedis.get("user:" + username + ":password");
            if (StringUtils.isNotBlank(passwordCache)) {
                String memberCache = jedis.get("user:" + passwordCache + ":info");
                if (StringUtils.isNotBlank(memberCache)) {
                    umsMember1 = JSON.parseObject(memberCache, UmsMember.class);
                }else {
                    return null;
                }
            }else{
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return umsMember1;
    }
}
