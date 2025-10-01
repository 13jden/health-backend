package com.dzk.wx.api.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "预约数据传输对象")
public class AppointmentDto {
    
    @Schema(description = "预约ID")
    private Long id;
    
    @Schema(description = "家长ID")
    private Long parentId;
    
    @Schema(description = "医生ID")
    private Long doctorId;
    
    @Schema(description = "预约时间")
    private LocalDateTime appointmentTime;
    
    @Schema(description = "预约状态")
    private String status;

    @Data
    @Schema(description = "预约输入数据传输对象")
    public static class Input {
        @Schema(description = "家长ID")
        private Long parentId;
        
        @Schema(description = "医生ID")
        private Long doctorId;
        
        @Schema(description = "预约时间")
        private LocalDateTime appointmentTime;
        
        @Schema(description = "预约状态")
        private String status;
    }

    @Data
    @Schema(description = "预约详情数据传输对象")
    public static class Detail {
        @Schema(description = "预约ID")
        private Long id;
        
        @Schema(description = "家长ID")
        private Long parentId;
        
        @Schema(description = "医生ID")
        private Long doctorId;
        
        @Schema(description = "预约时间")
        private LocalDateTime appointmentTime;
        
        @Schema(description = "预约状态")
        private String status;
        
        @Schema(description = "创建时间")
        private String createTime;
        
        @Schema(description = "更新时间")
        private String updateTime;
    }
}
