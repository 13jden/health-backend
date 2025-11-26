package com.dzk.wx.api.child;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

    @Select("""
        SELECT *
        FROM child c
        WHERE
          -- report 表 15 天内没有数据
          NOT EXISTS (
                SELECT 1
                FROM report r
                WHERE r.child_id = c.id
                  AND r.create_time >= DATE_SUB(NOW(), INTERVAL 15 DAY)
            )
          -- exercise_record 15 天内超过 n 条
          AND (
                SELECT COUNT(*)
                FROM exercise_record er
                WHERE er.child_id = c.id
                  AND er.record_time >= DATE_SUB(NOW(), INTERVAL 15 DAY)
              ) > #{limitCount}
          -- diet_record 15 天内超过 n 条
          AND (
                SELECT COUNT(*)
                FROM diet_record dr
                WHERE dr.child_id = c.id
                  AND dr.record_time >= DATE_SUB(NOW(), INTERVAL 15 DAY)
              ) > #{limitCount}
        """)
    List<Child> selectReportChild(@Param("limitCount") Integer limitCount);

}
