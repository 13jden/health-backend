package com.dzk.wx.api.dietrecord;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietRecordService extends ServiceImpl<DietRecordMapper, DietRecord> {

    @Autowired
    private DietRecordMapper dietRecordMapper;

    @Autowired
    private DietRecordConverter dietRecordConverter;

    /**
     * 添加饮食记录
     */
    @Transactional
    public DietRecordDto.Detail addRecord(DietRecordDto.Input input) {
        DietRecord record = dietRecordConverter.toEntity(input);
        int result = dietRecordMapper.insert(record);
        if (result > 0) {
            DietRecord savedRecord = dietRecordMapper.getRecordById(record.getId());
            return dietRecordConverter.toDetail(savedRecord);
        }
        throw new RuntimeException("添加饮食记录失败");
    }

    /**
     * 根据ID获取饮食记录详情
     */
    public DietRecordDto.Detail getRecordById(Long id) {
        DietRecord record = dietRecordMapper.getRecordById(id);
        if (record == null) {
            throw new RuntimeException("饮食记录不存在");
        }
        return dietRecordConverter.toDetail(record);
    }

    /**
     * 根据儿童ID获取饮食记录列表
     */
    public List<DietRecordDto.Detail> getRecordsByChildId(Long childId) {
        List<DietRecord> records = dietRecordMapper.getRecordsByChildId(childId);
        return records.stream()
                .map(dietRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 根据儿童ID和日期获取饮食记录
     */
    public List<DietRecordDto.Detail> getRecordsByChildIdAndDate(Long childId, LocalDate recordDate) {
        List<DietRecord> records = dietRecordMapper.getRecordsByChildIdAndDate(childId, recordDate);
        return records.stream()
                .map(dietRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 获取儿童最新的几条饮食记录
     */
    public List<DietRecordDto.Detail> getLatestRecordsByChildId(Long childId, Integer limit) {
        List<DietRecord> records = dietRecordMapper.getLatestRecordsByChildId(childId, limit);
        return records.stream()
                .map(dietRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 根据儿童ID、餐次类型和日期获取饮食记录
     */
    public List<DietRecordDto.Detail> getRecordsByChildIdAndMealTypeAndDate(Long childId, String mealType, LocalDate recordDate) {
        List<DietRecord> records = dietRecordMapper.getRecordsByChildIdAndMealTypeAndDate(childId, mealType, recordDate);
        return records.stream()
                .map(dietRecordConverter::toDetail)
                .collect(Collectors.toList());
    }

    /**
     * 更新饮食记录
     */
    @Transactional
    public DietRecordDto.Detail updateRecord(Long id, DietRecordDto.Input input) {
        DietRecord existingRecord = dietRecordMapper.getRecordById(id);
        if (existingRecord == null) {
            throw new RuntimeException("饮食记录不存在");
        }

        // 更新字段
        existingRecord.setChildId(input.getChildId());
        existingRecord.setMealType(input.getMealType());
        existingRecord.setFoodName(input.getFoodName());
        existingRecord.setFoodCategory(input.getFoodCategory());
        existingRecord.setQuantity(input.getQuantity());
        existingRecord.setUnit(input.getUnit());
        existingRecord.setCalories(input.getCalories());
        existingRecord.setProtein(input.getProtein());
        existingRecord.setCarbohydrate(input.getCarbohydrate());
        existingRecord.setFat(input.getFat());
        existingRecord.setFiber(input.getFiber());
        existingRecord.setRecordDate(input.getRecordDate());
        existingRecord.setRecordTime(input.getRecordTime());
        existingRecord.setNotes(input.getNotes());

        int result = dietRecordMapper.updateById(existingRecord);
        if (result > 0) {
            DietRecord updatedRecord = dietRecordMapper.getRecordById(id);
            return dietRecordConverter.toDetail(updatedRecord);
        }
        throw new RuntimeException("更新饮食记录失败");
    }

    /**
     * 删除饮食记录
     */
    @Transactional
    public void deleteRecord(Long id) {
        DietRecord existingRecord = dietRecordMapper.getRecordById(id);
        if (existingRecord == null) {
            throw new RuntimeException("饮食记录不存在");
        }
        
        int result = dietRecordMapper.deleteById(id);
        if (result <= 0) {
            throw new RuntimeException("删除饮食记录失败");
        }
    }

    /**
     * 根据儿童ID删除所有饮食记录
     */
    @Transactional
    public void deleteRecordsByChildId(Long childId) {
        int result = dietRecordMapper.delete(
                new QueryWrapper<DietRecord>().eq("child_id", childId)
        );
        if (result < 0) {
            throw new RuntimeException("删除饮食记录失败");
        }
    }
}

