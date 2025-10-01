package com.dzk.wx.api.message;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService extends ServiceImpl<MessageMapper, Message> {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageConverter messageConverter;

    public List<Message> getMessagesByReceiverId(Long receiverId) {
        return messageMapper.getMessagesByReceiverId(receiverId);
    }

    public List<Message> getMessagesByReceiverRole(String receiverRole) {
        return messageMapper.getMessagesByReceiverRole(receiverRole);
    }

    public Message getMessageById(Long id) {
        return messageMapper.getMessageById(id);
    }

    public Message saveMessage(Message message) {
        int result = messageMapper.insert(message);
        return messageMapper.getMessageById(message.getId());
    }

    public boolean updateMessage(Message message) {
        return messageMapper.updateById(message) > 0;
    }

    public boolean deleteMessage(Long id) {
        return messageMapper.deleteById(id) > 0;
    }
} 