package com.dzk.wx.api.feedback;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "反馈数据传输对象")
public class FeedbackDto {
    
    @Schema(description = "反馈ID")
    private Long id;
    
    @Schema(description = "家长ID")
    private Long parentId;
    
    @Schema(description = "反馈内容")
    private String content;
    
    @Schema(description = "回复内容")
    private String reply;

    @Data
    @Schema(description = "反馈输入数据传输对象")
    public static class Input {
        @Schema(description = "家长ID")
        private Long parentId;
        
        @Schema(description = "反馈内容")
        private String content;
    }

    @Data
    @Schema(description = "反馈详情数据传输对象")
    public static class Detail {
        @Schema(description = "反馈ID")
        private Long id;
        
        @Schema(description = "家长ID")
        private Long parentId;
        
        @Schema(description = "反馈内容")
        private String content;
        
        @Schema(description = "回复内容")
        private String reply;
        
        @Schema(description = "创建时间")
        private String createTime;
        
        @Schema(description = "更新时间")
        private String updateTime;
    }
} 