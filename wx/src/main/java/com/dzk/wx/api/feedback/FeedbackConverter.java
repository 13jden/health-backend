package com.dzk.wx.api.feedback;

import org.springframework.stereotype.Component;

@Component
public class FeedbackConverter {
    
    public FeedbackDto toDto(Feedback feedback) {
        if (feedback == null) {
            return null;
        }
        
        FeedbackDto dto = new FeedbackDto();
        dto.setId(feedback.getId());
        dto.setParentId(feedback.getParentId());
        dto.setContent(feedback.getContent());
        dto.setReply(feedback.getReply());
        return dto;
    }
    
    public Feedback toEntity(FeedbackDto.Input input) {
        if (input == null) {
            return null;
        }
        
        Feedback feedback = new Feedback();
        feedback.setParentId(input.getParentId());
        feedback.setContent(input.getContent());
        return feedback;
    }

    public FeedbackDto.Detail toDetail(Feedback feedback) {
        if (feedback == null) {
            return null;
        }
        
        FeedbackDto.Detail detail = new FeedbackDto.Detail();
        detail.setId(feedback.getId());
        detail.setParentId(feedback.getParentId());
        detail.setContent(feedback.getContent());
        detail.setReply(feedback.getReply());
        detail.setCreateTime(feedback.getCreateTime() != null ? feedback.getCreateTime().toString() : null);
        detail.setUpdateTime(feedback.getUpdateTime() != null ? feedback.getUpdateTime().toString() : null);
        return detail;
    }
} 