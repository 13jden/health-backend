package com.dzk.wx.api.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper extends BaseMapper<Log> {
    
    @Select("SELECT * FROM log WHERE user_id = #{userId}")
    List<Log> getLogsByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM log WHERE id = #{id}")
    Log getLogById(@Param("id") Long id);
}