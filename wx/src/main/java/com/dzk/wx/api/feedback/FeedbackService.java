package com.dzk.wx.api.feedback;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService extends ServiceImpl<FeedbackMapper, Feedback> {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private FeedbackConverter feedbackConverter;

    public List<Feedback> getFeedbacksByParentId(Long parentId) {
        return feedbackMapper.getFeedbacksByParentId(parentId);
    }

    public Feedback getFeedbackById(Long id) {
        return feedbackMapper.getFeedbackById(id);
    }

    public Feedback saveFeedback(Feedback feedback) {
        int result = feedbackMapper.insert(feedback);
        return feedbackMapper.getFeedbackById(feedback.getId());
    }

    public boolean updateFeedback(Feedback feedback) {
        return feedbackMapper.updateById(feedback) > 0;
    }

    public boolean deleteFeedback(Long id) {
        return feedbackMapper.deleteById(id) > 0;
    }
} 