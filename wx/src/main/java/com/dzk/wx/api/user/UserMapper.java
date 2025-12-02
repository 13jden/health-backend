package com.dzk.wx.api.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    @Select("SELECT * FROM user WHERE open_id = #{openId}")
    User getUserByOpenId(@Param("openId") String openId);
    
    @Select("SELECT * FROM user WHERE username = #{username}")
    User getUserByUsername(@Param("username") String username);

    @Select("SELECT * FROM user WHERE id IN (SELECT DISTINCT parent_id FROM child)")
    List<User> selectListBychild();
}
