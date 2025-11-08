package com.dzk.wx.api.growthrecord;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface GrowthRecordMapper extends BaseMapper<GrowthRecord> {
    
    @Select("SELECT * FROM growth_record WHERE child_id = #{childId} ORDER BY test_date DESC")
    List<GrowthRecord> getRecordsByChildId(@Param("childId") Long childId);
    
    @Select("SELECT * FROM growth_record WHERE child_id = #{childId} ORDER BY test_date DESC LIMIT 1")
    GrowthRecord getRecordByChildId(@Param("childId") Long childId);

    @Select("SELECT * FROM growth_record WHERE id = #{id}")
    GrowthRecord getRecordById(@Param("id") Long id);
    
    @Select("SELECT * FROM growth_record WHERE child_id = #{childId} AND test_date = #{testDate}")
    GrowthRecord getRecordByChildIdAndDate(@Param("childId") Long childId, @Param("testDate") String testDate);
    
    @Select("SELECT * FROM growth_record WHERE child_id = #{childId} ORDER BY test_date DESC LIMIT #{limit}")
    List<GrowthRecord> getLatestRecordsByChildId(@Param("childId") Long childId, @Param("limit") Integer limit);

    @Select("SELECT test_date FROM growth_record WHERE child_id = #{childId} ORDER BY test_date DESC LIMIT 1")
    LocalDate getLastTestDate(@Param("childId") Long childId);
}
