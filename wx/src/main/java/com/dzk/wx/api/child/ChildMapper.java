package com.dzk.wx.api.child;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChildMapper extends BaseMapper<Child> {
    
    @Select("SELECT * FROM child WHERE parent_id = #{parentId}")
    List<Child> getChildrenByParentId(@Param("parentId") Long parentId);
    
    @Select("SELECT * FROM child WHERE id = #{id}")
    Child getChildById(@Param("id") Long id);
}
