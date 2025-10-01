package com.dzk.wx.api.feedback;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
    
    @Select("SELECT * FROM feedback WHERE parent_id = #{parentId}")
    List<Feedback> getFeedbacksByParentId(@Param("parentId") Long parentId);
    
    @Select("SELECT * FROM feedback WHERE id = #{id}")
    Feedback getFeedbackById(@Param("id") Long id);
} 