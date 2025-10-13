package com.dzk.wx.api.exerciserecord;

import org.springframework.stereotype.Component;

@Component
public class ExerciseRecordConverter {
    
    /**
     * 将ExerciseRecord实体转换为ExerciseRecordDto
     */
    public ExerciseRecordDto toDto(ExerciseRecord exerciseRecord) {
        if (exerciseRecord == null) {
            return null;
        }
        
        ExerciseRecordDto exerciseRecordDto = new ExerciseRecordDto();
        exerciseRecordDto.setId(exerciseRecord.getId());
        exerciseRecordDto.setChildId(exerciseRecord.getChildId());
        exerciseRecordDto.setExerciseType(exerciseRecord.getExerciseType());
        exerciseRecordDto.setExerciseCategory(exerciseRecord.getExerciseCategory());
        exerciseRecordDto.setDuration(exerciseRecord.getDuration());
        exerciseRecordDto.setIntensity(exerciseRecord.getIntensity());
        exerciseRecordDto.setCaloriesBurned(exerciseRecord.getCaloriesBurned());
        exerciseRecordDto.setDistance(exerciseRecord.getDistance());
        exerciseRecordDto.setSteps(exerciseRecord.getSteps());
        exerciseRecordDto.setHeartRateAvg(exerciseRecord.getHeartRateAvg());
        exerciseRecordDto.setHeartRateMax(exerciseRecord.getHeartRateMax());
        exerciseRecordDto.setRecordDate(exerciseRecord.getRecordDate());
        exerciseRecordDto.setStartTime(exerciseRecord.getStartTime());
        exerciseRecordDto.setEndTime(exerciseRecord.getEndTime());
        exerciseRecordDto.setNotes(exerciseRecord.getNotes());
        return exerciseRecordDto;
    }
    
    /**
     * 将ExerciseRecordDto.Input转换为ExerciseRecord实体
     */
    public ExerciseRecord toEntity(ExerciseRecordDto.Input input) {
        if (input == null) {
            return null;
        }
        
        ExerciseRecord exerciseRecord = new ExerciseRecord();
        exerciseRecord.setChildId(input.getChildId());
        exerciseRecord.setExerciseType(input.getExerciseType());
        exerciseRecord.setExerciseCategory(input.getExerciseCategory());
        exerciseRecord.setDuration(input.getDuration());
        exerciseRecord.setIntensity(input.getIntensity());
        exerciseRecord.setCaloriesBurned(input.getCaloriesBurned());
        exerciseRecord.setDistance(input.getDistance());
        exerciseRecord.setSteps(input.getSteps());
        exerciseRecord.setHeartRateAvg(input.getHeartRateAvg());
        exerciseRecord.setHeartRateMax(input.getHeartRateMax());
        exerciseRecord.setRecordDate(input.getRecordDate());
        exerciseRecord.setStartTime(input.getStartTime());
        exerciseRecord.setEndTime(input.getEndTime());
        exerciseRecord.setNotes(input.getNotes());
        return exerciseRecord;
    }

    /**
     * 将ExerciseRecord实体转换为ExerciseRecordDto.Detail
     */
    public ExerciseRecordDto.Detail toDetail(ExerciseRecord exerciseRecord) {
        if (exerciseRecord == null) {
            return null;
        }
        
        ExerciseRecordDto.Detail detail = new ExerciseRecordDto.Detail();
        detail.setId(exerciseRecord.getId());
        detail.setChildId(exerciseRecord.getChildId());
        detail.setExerciseType(exerciseRecord.getExerciseType());
        detail.setExerciseCategory(exerciseRecord.getExerciseCategory());
        detail.setDuration(exerciseRecord.getDuration());
        detail.setIntensity(exerciseRecord.getIntensity());
        detail.setCaloriesBurned(exerciseRecord.getCaloriesBurned());
        detail.setDistance(exerciseRecord.getDistance());
        detail.setSteps(exerciseRecord.getSteps());
        detail.setHeartRateAvg(exerciseRecord.getHeartRateAvg());
        detail.setHeartRateMax(exerciseRecord.getHeartRateMax());
        detail.setRecordDate(exerciseRecord.getRecordDate());
        detail.setStartTime(exerciseRecord.getStartTime());
        detail.setEndTime(exerciseRecord.getEndTime());
        detail.setNotes(exerciseRecord.getNotes());
        detail.setCreateTime(exerciseRecord.getCreateTime() != null ? exerciseRecord.getCreateTime().toString() : null);
        detail.setUpdateTime(exerciseRecord.getUpdateTime() != null ? exerciseRecord.getUpdateTime().toString() : null);
        return detail;
    }
}
