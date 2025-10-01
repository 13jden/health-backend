package com.dzk.wx.api.message;

import org.springframework.stereotype.Component;

@Component
public class MessageConverter {
    
    public MessageDto toDto(Message message) {
        if (message == null) {
            return null;
        }
        
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setReceiverId(message.getReceiverId());
        dto.setReceiverRole(message.getReceiverRole());
        dto.setMessage(message.getMessage());
        dto.setStatus(message.getStatus());
        return dto;
    }
    
    public Message toEntity(MessageDto.Input input) {
        if (input == null) {
            return null;
        }
        
        Message message = new Message();
        message.setReceiverId(input.getReceiverId());
        message.setReceiverRole(input.getReceiverRole());
        message.setMessage(input.getMessage());
        message.setStatus(input.getStatus());
        return message;
    }

    public MessageDto.Detail toDetail(Message message) {
        if (message == null) {
            return null;
        }
        
        MessageDto.Detail detail = new MessageDto.Detail();
        detail.setId(message.getId());
        detail.setReceiverId(message.getReceiverId());
        detail.setReceiverRole(message.getReceiverRole());
        detail.setMessage(message.getMessage());
        detail.setStatus(message.getStatus());
        detail.setCreateTime(message.getCreateTime() != null ? message.getCreateTime().toString() : null);
        detail.setUpdateTime(message.getUpdateTime() != null ? message.getUpdateTime().toString() : null);
        return detail;
    }
}