package com.dzk.wx.api.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "通知数据传输对象")
public class NotificationDto {
    
    @Schema(description = "通知ID")
    private Long id;
    
    @Schema(description = "接收者ID")
    private Long receiverId;
    
    @Schema(description = "接收者角色")
    private String receiverRole;
    
    @Schema(description = "通知内容")
    private String message;
    
    @Schema(description = "通知状态")
    private String status;

    @Data
    @Schema(description = "通知输入数据传输对象")
    public static class Input {
        @Schema(description = "接收者ID")
        private Long receiverId;
        
        @Schema(description = "接收者角色")
        private String receiverRole;
        
        @Schema(description = "通知内容")
        private String message;
        
        @Schema(description = "通知状态")
        private String status;
    }

    @Data
    @Schema(description = "通知详情数据传输对象")
    public static class Detail {
        @Schema(description = "通知ID")
        private Long id;
        
        @Schema(description = "接收者ID")
        private Long receiverId;
        
        @Schema(description = "接收者角色")
        private String receiverRole;
        
        @Schema(description = "通知内容")
        private String message;
        
        @Schema(description = "通知状态")
        private String status;
        
        @Schema(description = "创建时间")
        private String createTime;
        
        @Schema(description = "更新时间")
        private String updateTime;
    }
}
