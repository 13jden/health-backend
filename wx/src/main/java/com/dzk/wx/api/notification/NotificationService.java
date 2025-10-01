package com.dzk.wx.api.notification;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService extends ServiceImpl<NotificationMapper, Notification> {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationConverter notificationConverter;

    public List<Notification> getNotificationsByReceiverId(Long receiverId) {
        return notificationMapper.getNotificationsByReceiverId(receiverId);
    }

    public List<Notification> getNotificationsByReceiverRole(String receiverRole) {
        return notificationMapper.getNotificationsByReceiverRole(receiverRole);
    }

    public Notification getNotificationById(Long id) {
        return notificationMapper.getNotificationById(id);
    }

    public Notification saveNotification(Notification notification) {
        int result = notificationMapper.insert(notification);
        return notificationMapper.getNotificationById(notification.getId());
    }

    public boolean updateNotification(Notification notification) {
        return notificationMapper.updateById(notification) > 0;
    }

    public boolean deleteNotification(Long id) {
        return notificationMapper.deleteById(id) > 0;
    }
}