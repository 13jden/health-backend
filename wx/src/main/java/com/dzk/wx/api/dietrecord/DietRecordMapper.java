package com.dzk.wx.api.dietrecord;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DietRecordMapper extends BaseMapper<DietRecord> {
    
    @Results(id = "dietRecordResultMap", value = {
        @Result(column = "image_list", property = "imageList",
                typeHandler = JacksonTypeHandler.class)
    })
    @Select("SELECT * FROM diet_record WHERE child_id = #{childId} ORDER BY record_date DESC, record_time DESC")
    List<DietRecord> getRecordsByChildId(@Param("childId") Long childId);

    @ResultMap("dietRecordResultMap")
    @Select("SELECT * FROM diet_record WHERE id = #{id}")
    DietRecord getRecordById(@Param("id") Long id);
    
    @Select("SELECT * FROM diet_record WHERE child_id = #{childId} AND record_date = #{recordDate} ORDER BY record_time DESC")
    List<DietRecord> getRecordsByChildIdAndDate(@Param("childId") Long childId, @Param("recordDate") LocalDate recordDate);
    
    @Select("SELECT * FROM diet_record WHERE child_id = #{childId} ORDER BY record_date DESC, record_time DESC LIMIT #{limit}")
    List<DietRecord> getLatestRecordsByChildId(@Param("childId") Long childId, @Param("limit") Integer limit);
    
    @Select("SELECT * FROM diet_record WHERE child_id = #{childId} AND meal_type = #{mealType} AND record_date = #{recordDate} ORDER BY record_time DESC")
    List<DietRecord> getRecordsByChildIdAndMealTypeAndDate(@Param("childId") Long childId, @Param("mealType") String mealType, @Param("recordDate") LocalDate recordDate);
}
