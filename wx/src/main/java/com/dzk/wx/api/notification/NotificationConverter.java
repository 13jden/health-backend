package com.dzk.wx.api.notification;

import org.springframework.stereotype.Component;

@Component
public class NotificationConverter {
    
    public NotificationDto toDto(Notification notification) {
        if (notification == null) {
            return null;
        }
        
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setReceiverId(notification.getReceiverId());
        dto.setReceiverRole(notification.getReceiverRole());
        dto.setMessage(notification.getMessage());
        dto.setStatus(notification.getStatus());
        return dto;
    }
    
    public Notification toEntity(NotificationDto.Input input) {
        if (input == null) {
            return null;
        }
        
        Notification notification = new Notification();
        notification.setReceiverId(input.getReceiverId());
        notification.setReceiverRole(input.getReceiverRole());
        notification.setMessage(input.getMessage());
        notification.setStatus(input.getStatus());
        return notification;
    }

    public NotificationDto.Detail toDetail(Notification notification) {
        if (notification == null) {
            return null;
        }
        
        NotificationDto.Detail detail = new NotificationDto.Detail();
        detail.setId(notification.getId());
        detail.setReceiverId(notification.getReceiverId());
        detail.setReceiverRole(notification.getReceiverRole());
        detail.setMessage(notification.getMessage());
        detail.setStatus(notification.getStatus());
        detail.setCreateTime(notification.getCreateTime() != null ? notification.getCreateTime().toString() : null);
        detail.setUpdateTime(notification.getUpdateTime() != null ? notification.getUpdateTime().toString() : null);
        return detail;
    }
}