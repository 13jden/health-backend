package com.dzk.wx.api.predictionresult;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PredictionResultMapper extends BaseMapper<PredictionResult> {
    
    @Select("SELECT * FROM prediction_result WHERE child_id = #{childId}")
    List<PredictionResult> getResultsByChildId(@Param("childId") Long childId);
    
    @Select("SELECT * FROM prediction_result WHERE id = #{id}")
    PredictionResult getResultById(@Param("id") Long id);
} 