 package com.dzk.wx.api.log;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "日志数据传输对象")
public class LogDto {
    
    @Schema(description = "日志ID")
    private Long id;
    
    @Schema(description = "操作动作")
    private String action;
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "操作时间")
    private LocalDateTime actionTime;
    
    @Schema(description = "操作详情")
    private String details;

    @Data
    @Schema(description = "日志输入数据传输对象")
    public static class Input {
        @Schema(description = "操作动作")
        private String action;
        
        @Schema(description = "用户ID")
        private Long userId;
        
        @Schema(description = "操作时间")
        private LocalDateTime actionTime;
        
        @Schema(description = "操作详情")
        private String details;
    }

    @Data
    @Schema(description = "日志详情数据传输对象")
    public static class Detail {
        @Schema(description = "日志ID")
        private Long id;
        
        @Schema(description = "操作动作")
        private String action;
        
        @Schema(description = "用户ID")
        private Long userId;
        
        @Schema(description = "操作时间")
        private LocalDateTime actionTime;
        
        @Schema(description = "操作详情")
        private String details;
        
        @Schema(description = "创建时间")
        private String createTime;
        
        @Schema(description = "更新时间")
        private String updateTime;
    }
}