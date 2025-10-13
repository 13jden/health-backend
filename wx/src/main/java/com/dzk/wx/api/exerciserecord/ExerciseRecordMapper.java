package com.dzk.wx.api.exerciserecord;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ExerciseRecordMapper extends BaseMapper<ExerciseRecord> {
    
    @Select("SELECT * FROM exercise_record WHERE child_id = #{childId} ORDER BY record_date DESC, start_time DESC")
    List<ExerciseRecord> getRecordsByChildId(@Param("childId") Long childId);
    
    @Select("SELECT * FROM exercise_record WHERE id = #{id}")
    ExerciseRecord getRecordById(@Param("id") Long id);
    
    @Select("SELECT * FROM exercise_record WHERE child_id = #{childId} AND record_date = #{recordDate} ORDER BY start_time DESC")
    List<ExerciseRecord> getRecordsByChildIdAndDate(@Param("childId") Long childId, @Param("recordDate") LocalDate recordDate);
    
    @Select("SELECT * FROM exercise_record WHERE child_id = #{childId} ORDER BY record_date DESC, start_time DESC LIMIT #{limit}")
    List<ExerciseRecord> getLatestRecordsByChildId(@Param("childId") Long childId, @Param("limit") Integer limit);
    
    @Select("SELECT * FROM exercise_record WHERE child_id = #{childId} AND exercise_type = #{exerciseType} ORDER BY record_date DESC, start_time DESC")
    List<ExerciseRecord> getRecordsByChildIdAndExerciseType(@Param("childId") Long childId, @Param("exerciseType") String exerciseType);
}
