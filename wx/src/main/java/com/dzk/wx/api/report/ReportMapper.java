package com.dzk.wx.api.report;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface ReportMapper extends BaseMapper<Report> {

    @Select("SELECT * FROM report WHERE child_id = #{childId}")
    List<Report> selectByChildId(Long childId);
}
