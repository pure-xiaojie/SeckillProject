package com.xiaojie.dao;

import com.xiaojie.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 用户持久层
 */
@Repository
@Mapper
public interface UserMapper {
    @Select("select * from user where phone = #{phone} and password = #{password} ")
    User selectByPhoneAndPassword(@Param("phone") String phone , @Param("password") String password);

    @Select("select * from user where phone = #{phone} ")
    User checkPhone(@Param("phone") String phone );
}
