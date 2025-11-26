package com.dzk.wx.api.dietrecord;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DietRecordConverter {
    
    /**
     * 将DietRecord实体转换为DietRecordDto
     */
    public DietRecordDto toDto(DietRecord dietRecord) {
        if (dietRecord == null) {
            return null;
        }
        
        DietRecordDto dietRecordDto = new DietRecordDto();
        dietRecordDto.setId(dietRecord.getId());
        dietRecordDto.setChildId(dietRecord.getChildId());
        dietRecordDto.setMealType(dietRecord.getMealType());
        dietRecordDto.setFoodName(dietRecord.getFoodName());
        dietRecordDto.setFoodCategory(dietRecord.getFoodCategory());
        dietRecordDto.setQuantity(dietRecord.getQuantity());
        dietRecordDto.setUnit(dietRecord.getUnit());
        dietRecordDto.setCalories(dietRecord.getCalories());
        dietRecordDto.setProtein(dietRecord.getProtein());
        dietRecordDto.setCarbohydrate(dietRecord.getCarbohydrate());
        dietRecordDto.setFat(dietRecord.getFat());
        dietRecordDto.setFiber(dietRecord.getFiber());
        dietRecordDto.setRecordDate(dietRecord.getRecordDate());
        dietRecordDto.setRecordTime(dietRecord.getRecordTime());
        dietRecordDto.setNotes(dietRecord.getNotes());
        dietRecordDto.setImageList(dietRecord.getImageList());
        return dietRecordDto;
    }

    public List<DietRecordDto> toDtoList(List<DietRecord> dietRecords){
        List<DietRecordDto> dietRecordDtoList = new ArrayList<>();
        for (DietRecord dietRecord : dietRecords){
            dietRecordDtoList.add(toDto(dietRecord));
        }
        return dietRecordDtoList;
    }
    
    /**
     * 将DietRecordDto.Input转换为DietRecord实体
     */
    public DietRecord toEntity(DietRecordDto.Input input) {
        if (input == null) {
            return null;
        }
        
        DietRecord dietRecord = new DietRecord();
        dietRecord.setChildId(input.getChildId());
        dietRecord.setMealType(input.getMealType());
        dietRecord.setFoodName(input.getFoodName());
        dietRecord.setFoodCategory(input.getFoodCategory());
        dietRecord.setQuantity(input.getQuantity());
        dietRecord.setUnit(input.getUnit());
        dietRecord.setCalories(input.getCalories());
        dietRecord.setProtein(input.getProtein());
        dietRecord.setCarbohydrate(input.getCarbohydrate());
        dietRecord.setFat(input.getFat());
        dietRecord.setFiber(input.getFiber());
        dietRecord.setRecordDate(input.getRecordDate());
        dietRecord.setRecordTime(input.getRecordTime());
        dietRecord.setNotes(input.getNotes());
        dietRecord.setImageList(input.getImageList());
        return dietRecord;
    }

    /**
     * 将DietRecord实体转换为DietRecordDto.Detail
     */
    public DietRecordDto.Detail toDetail(DietRecord dietRecord) {
        if (dietRecord == null) {
            return null;
        }
        
        DietRecordDto.Detail detail = new DietRecordDto.Detail();
        detail.setId(dietRecord.getId());
        detail.setChildId(dietRecord.getChildId());
        detail.setMealType(dietRecord.getMealType());
        detail.setFoodName(dietRecord.getFoodName());
        detail.setFoodCategory(dietRecord.getFoodCategory());
        detail.setQuantity(dietRecord.getQuantity());
        detail.setUnit(dietRecord.getUnit());
        detail.setCalories(dietRecord.getCalories());
        detail.setProtein(dietRecord.getProtein());
        detail.setCarbohydrate(dietRecord.getCarbohydrate());
        detail.setFat(dietRecord.getFat());
        detail.setFiber(dietRecord.getFiber());
        detail.setRecordDate(dietRecord.getRecordDate());
        detail.setRecordTime(dietRecord.getRecordTime());
        detail.setNotes(dietRecord.getNotes());
        detail.setImageList(dietRecord.getImageList());
        detail.setCreateTime(dietRecord.getCreateTime() != null ? dietRecord.getCreateTime().toString() : null);
        detail.setUpdateTime(dietRecord.getUpdateTime() != null ? dietRecord.getUpdateTime().toString() : null);
        return detail;
    }
}
