package com.fendou.gmall.service;

import com.fendou.gmall.bean.UmsMember;

import java.util.List;

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
}
