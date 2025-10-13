package com.dzk.wx.api.growthrecord;

import org.springframework.stereotype.Component;

@Component
public class GrowthRecordConverter {
    
    /**
     * 将GrowthRecord实体转换为GrowthRecordDto
     */
    public GrowthRecordDto toDto(GrowthRecord record) {
        if (record == null) {
            return null;
        }
        
        GrowthRecordDto dto = new GrowthRecordDto();
        dto.setId(record.getId());
        dto.setChildId(record.getChildId());
        dto.setHeight(record.getHeight());
        dto.setWeight(record.getWeight());
        dto.setBmi(record.getBmi());
        dto.setBoneAge(record.getBoneAge());
        dto.setTestDate(record.getTestDate());
        return dto;
    }
    
    /**
     * 将GrowthRecordDto.Input转换为GrowthRecord实体
     */
    public GrowthRecord toEntity(GrowthRecordDto.Input input) {
        if (input == null) {
            return null;
        }
        
        GrowthRecord record = new GrowthRecord();
        record.setChildId(input.getChildId());
        record.setHeight(input.getHeight());
        record.setWeight(input.getWeight());
        record.setBmi(input.getBmi());
        record.setBoneAge(input.getBoneAge());
        record.setTestDate(input.getTestDate());
        return record;
    }

    /**
     * 将GrowthRecord实体转换为GrowthRecordDto.Detail
     */
    public GrowthRecordDto.Detail toDetail(GrowthRecord record) {
        if (record == null) {
            return null;
        }
        
        GrowthRecordDto.Detail detail = new GrowthRecordDto.Detail();
        detail.setId(record.getId());
        detail.setChildId(record.getChildId());
        detail.setHeight(record.getHeight());
        detail.setWeight(record.getWeight());
        detail.setBmi(record.getBmi());
        detail.setBoneAge(record.getBoneAge());
        detail.setTestDate(record.getTestDate());
        detail.setCreateTime(record.getCreateTime() != null ? record.getCreateTime().toString() : null);
        detail.setUpdateTime(record.getUpdateTime() != null ? record.getUpdateTime().toString() : null);
        return detail;
    }
}
