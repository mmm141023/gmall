package com.fendou.gmall.user.dao;

import com.fendou.gmall.bean.UmsMember;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<UmsMember> {

    @Select(value = "select * from ums_member")
    public List<UmsMember> getAllUser();
}
