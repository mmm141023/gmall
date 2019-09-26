package com.fendou.gmall.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.fendou.gmall.bean.UmsMember;
import com.fendou.gmall.service.UserService;
import com.fendou.gmall.user.dao.UmsMemberMapper;
import com.fendou.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Map;

/**
 * UserServiceImpl class
 *
 * @author maochaoying
 * @date 2019/8/30
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UmsMemberMapper umsMemberMapper;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 查询数据库是否存在umsMember
     * @param umsMember
     * @return
     */
    @Override
    public UmsMember queryUmsMemberOne(UmsMember umsMember) {
        UmsMember umsMember1 = umsMemberMapper.selectOne(umsMember);
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

    @Override
    public UmsMember saveWeiBoUserInfo(Map<String, String> user_map) {
        String access_token = user_map.get("access_token");
        String code = user_map.get("code");

        // 将信息存入数据库
        UmsMember umsMember = new UmsMember();
        umsMember.setSource_uid(user_map.get("idstr"));
        umsMember.setAccess_code(code);
        umsMember.setAccess_token(access_token);
        umsMember.setNickname(user_map.get("screen_name"));
        umsMember.setPhone("12345678979");
        umsMember.setMemberLevelId("4");
        umsMember.setStatus("1");
        umsMember.setCreateTime(new Date());
        umsMember.setIcon(user_map.get("avatar_large"));
        String gender = user_map.get("gender");
        if (gender.equals("m")) {
            umsMember.setGender("1");
        }else if (gender.equals("f")) {
            umsMember.setGender("2");
        }else {
            umsMember.setGender("0");
        }
        umsMember.setBirthday(new Date());
        umsMember.setCity(user_map.get("location"));
        umsMember.setJob("程序员");
        umsMember.setPersonalizedSignature(user_map.get("description"));
        umsMember.setSourceType("2");
        umsMember.setIntegration("0");
        umsMember.setGrowth("0");
        umsMember.setLuckeyCount("0");
        umsMember.setHistoryIntegration("0");

        // 查询数据库中有没有该数据
        UmsMember umsMember1 = new UmsMember();
        umsMember1.setSource_uid(user_map.get("idstr"));
        UmsMember umsMember2 = umsMemberMapper.selectOne(umsMember1);
        if (umsMember2 == null) {
            umsMemberMapper.insertSelective(umsMember);
        }else{
            Example example = new Example(UmsMember.class);
            example.createCriteria().andEqualTo("source_uid", user_map.get("idstr"));
            umsMemberMapper.updateByExampleSelective(umsMember, example);
        }
        return umsMember;
    }

    @Override
    public String getUsernameByMemberId(String memberId) {
        UmsMember umsMember = new UmsMember();
        umsMember.setId(memberId);
        UmsMember umsMember1 = umsMemberMapper.selectOne(umsMember);
        return umsMember1.getUsername();
    }
}
