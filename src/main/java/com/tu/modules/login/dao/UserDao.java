package com.tu.modules.login.dao;

import com.tu.common.bean.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {
    @Select("select * from sys_user where usercode = #{usercode}")
    @ResultType(User.class)
    User findUserByUsercode(@Param("usercode") String usercode);
}
