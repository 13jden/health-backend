package com.dzk.wx.api.message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    
    @Select("SELECT * FROM message WHERE receiver_id = #{receiverId}")
    List<Message> getMessagesByReceiverId(@Param("receiverId") Long receiverId);
    
    @Select("SELECT * FROM message WHERE receiver_role = #{receiverRole}")
    List<Message> getMessagesByReceiverRole(@Param("receiverRole") String receiverRole);
    
    @Select("SELECT * FROM message WHERE id = #{id}")
    Message getMessageById(@Param("id") Long id);
}