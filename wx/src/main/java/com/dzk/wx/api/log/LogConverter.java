package com.dzk.wx.api.log;

import org.springframework.stereotype.Component;

@Component
public class LogConverter {
    
    public LogDto toDto(Log log) {
        if (log == null) {
            return null;
        }
        
        LogDto dto = new LogDto();
        dto.setId(log.getId());
        dto.setAction(log.getAction());
        dto.setUserId(log.getUserId());
        dto.setActionTime(log.getActionTime());
        dto.setDetails(log.getDetails());
        return dto;
    }
    
    public Log toEntity(LogDto.Input input) {
        if (input == null) {
            return null;
        }
        
        Log log = new Log();
        log.setAction(input.getAction());
        log.setUserId(input.getUserId());
        log.setActionTime(input.getActionTime());
        log.setDetails(input.getDetails());
        return log;
    }

    public LogDto.Detail toDetail(Log log) {
        if (log == null) {
            return null;
        }
        
        LogDto.Detail detail = new LogDto.Detail();
        detail.setId(log.getId());
        detail.setAction(log.getAction());
        detail.setUserId(log.getUserId());
        detail.setActionTime(log.getActionTime());
        detail.setDetails(log.getDetails());
        detail.setCreateTime(log.getCreateTime() != null ? log.getCreateTime().toString() : null);
        detail.setUpdateTime(log.getUpdateTime() != null ? log.getUpdateTime().toString() : null);
        return detail;
    }
} 