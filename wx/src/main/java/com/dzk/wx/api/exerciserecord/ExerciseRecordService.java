package com.dzk.wx.api.exerciserecord;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dzk.common.exception.BusinessException;
import com.dzk.wx.api.exerciserecord.ai.ExerciseAgentTool;
import com.dzk.wx.api.growthrecord.GrowthRecord;
import com.dzk.wx.api.growthrecord.GrowthRecordMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseRecordService extends ServiceImpl<ExerciseRecordMapper, ExerciseRecord> {

    @Autowired
    private ExerciseRecordMapper exerciseRecordMapper;

    @Autowired
    private GrowthRecordMapper growthRecordMapper;

    @Autowired
    private ExerciseRecordConverter exerciseRecordConverter;

    @Autowired
    private ExerciseAgentTool exerciseAgentTool;

    @Autowired
    private Gson gson;


    /**
     * 快速ai识别content添加运动记录
     */
    public ExerciseRecordDto.Detail addQuickRecord(ExerciseRecordDto.QuickInput input) {
       GrowthRecord record = growthRecordMapper.getRecordByChildId(input.getChildId());
       if(record==null){
        throw new BusinessException("成长记录不存在，请先添加体测记录");
       }
       Integer weight = record.getWeight().intValue();
       String content = input.getContent();

       // 调用AI识别
       String jsonResult = exerciseAgentTool.recognizeExercise(content, weight);
       
       // 解析JSON为Input对象
       ExerciseRecordDto.Input recordInput = gson.fromJson(jsonResult, ExerciseRecordDto.Input.class);
       
       // 设置childId和recordDate（如果未设置）
       if (recordInput.getChildId() == null) {
           recordInput.setChildId(input.getChildId());
       }
       if (recordInput.getRecordDate() == null) {
           recordInput.setRecordDate(LocalDate.now());
       }
       
       // 调用addRecord插入记录
       return addRecord(recordInput);
    }

    /**
     * 添加运动记录
     */
    @Transactional
    public ExerciseRecordDto.Detail addRecord(ExerciseRecordDto.Input input) {
        ExerciseRecord record = exerciseRecordConverter.toEntity(input);
        int result = exerciseRecordMapper.insert(record);
        if (result > 0) {
            ExerciseRecord savedRecord = exerciseRecordMapper.getRecordById(record.getId());
            return exerciseRecordConverter.toDetail(savedRecord);
        }
        throw new RuntimeException("添加运动记录失败");
    }

    /**
     * 根据ID获取运动记录详情
     */
    public ExerciseRecordDto.Detail getRecordById(Long id) {
        ExerciseRecord record = exerciseRecordMapper.getRecordById(id);
        if (record == null) {
            throw new RuntimeException("运动记录不存在");
        }
        return exerciseRecordConverter.toDetail(record);
    }

    /**
     * 根据儿童ID获取运动记录列表
     */
    public List<ExerciseRecordDto.Detail> getRecordsByChildId(Long childId) {
        List<ExerciseRecord> records = exerciseRecordMapper.getRecordsByChildId(childId);
        return records.stream()
                .map(exerciseRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 根据儿童ID和日期获取运动记录
     */
    public List<ExerciseRecordDto.Detail> getRecordsByChildIdAndDate(Long childId, LocalDate recordDate) {
        List<ExerciseRecord> records = exerciseRecordMapper.getRecordsByChildIdAndDate(childId, recordDate);
        return records.stream()
                .map(exerciseRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 获取儿童最新的几条运动记录
     */
    public List<ExerciseRecordDto.Detail> getLatestRecordsByChildId(Long childId, Integer limit) {
        List<ExerciseRecord> records = exerciseRecordMapper.getLatestRecordsByChildId(childId, limit);
        return records.stream()
                .map(exerciseRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 根据儿童ID和运动类型获取运动记录
     */
    public List<ExerciseRecordDto.Detail> getRecordsByChildIdAndExerciseType(Long childId, String exerciseType) {
        List<ExerciseRecord> records = exerciseRecordMapper.getRecordsByChildIdAndExerciseType(childId, exerciseType);
        return records.stream()
                .map(exerciseRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 更新运动记录
     */
    @Transactional
    public ExerciseRecordDto.Detail updateRecord(Long id, ExerciseRecordDto.Input input) {
        ExerciseRecord existingRecord = exerciseRecordMapper.getRecordById(id);
        if (existingRecord == null) {
            throw new RuntimeException("运动记录不存在");
        }

        // 更新字段
        existingRecord.setChildId(input.getChildId());
        existingRecord.setExerciseType(input.getExerciseType());
        existingRecord.setExerciseCategory(input.getExerciseCategory());
        existingRecord.setDuration(input.getDuration());
        existingRecord.setIntensity(input.getIntensity());
        existingRecord.setCaloriesBurned(input.getCaloriesBurned());
        existingRecord.setDistance(input.getDistance());
        existingRecord.setSteps(input.getSteps());
        existingRecord.setHeartRateAvg(input.getHeartRateAvg());
        existingRecord.setHeartRateMax(input.getHeartRateMax());
        existingRecord.setRecordDate(input.getRecordDate());
        existingRecord.setStartTime(input.getStartTime());
        existingRecord.setEndTime(input.getEndTime());
        existingRecord.setNotes(input.getNotes());

        int result = exerciseRecordMapper.updateById(existingRecord);
        if (result > 0) {
            ExerciseRecord updatedRecord = exerciseRecordMapper.getRecordById(id);
            return exerciseRecordConverter.toDetail(updatedRecord);
        }
        throw new RuntimeException("更新运动记录失败");
    }

    /**
     * 删除运动记录
     */
    @Transactional
    public void deleteRecord(Long id) {
        ExerciseRecord existingRecord = exerciseRecordMapper.getRecordById(id);
        if (existingRecord == null) {
            throw new RuntimeException("运动记录不存在");
        }
        
        int result = exerciseRecordMapper.deleteById(id);
        if (result <= 0) {
            throw new RuntimeException("删除运动记录失败");
        }
    }

    /**
     * 根据儿童ID删除所有运动记录
     */
    @Transactional
    public void deleteRecordsByChildId(Long childId) {
        int result = exerciseRecordMapper.delete(
            new QueryWrapper<ExerciseRecord>().eq("child_id", childId)
        );
        if (result < 0) {
            throw new RuntimeException("删除运动记录失败");
        }
    }
}

