package com.fendou.gmall.service;

import com.fendou.gmall.bean.UmsMember;

import java.util.List;
import java.util.Map;

/**
 * UserService class
 *
 * @author maochaoying
 * @date 2019/8/30
 */

public interface UserService {
    UmsMember queryUmsMemberOne(UmsMember umsMember);

    void saveUserInfoCache(UmsMember umsMemberOne);

    UmsMember queryFromCache(UmsMember umsMember);

    UmsMember saveWeiBoUserInfo(Map<String, String> user_map);
}
