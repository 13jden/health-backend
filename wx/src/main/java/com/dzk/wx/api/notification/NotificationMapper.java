package com.dzk.wx.api.notification;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
    
    @Select("SELECT * FROM notification WHERE receiver_id = #{receiverId}")
    List<Notification> getNotificationsByReceiverId(@Param("receiverId") Long receiverId);
    
    @Select("SELECT * FROM notification WHERE receiver_role = #{receiverRole}")
    List<Notification> getNotificationsByReceiverRole(@Param("receiverRole") String receiverRole);
    
    @Select("SELECT * FROM notification WHERE id = #{id}")
    Notification getNotificationById(@Param("id") Long id);
}