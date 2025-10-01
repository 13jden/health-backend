package com.dzk.wx.api.healtharticle;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "健康文章数据传输对象")
public class HealthArticleDto {
    
    @Schema(description = "健康文章ID")
    private Long id;
    
    @Schema(description = "文章标题")
    private String title;
    
    @Schema(description = "文章内容")
    private String content;
    
    @Schema(description = "作者")
    private String author;
    
    @Schema(description = "发布日期")
    private LocalDate publishDate;

    @Data
    @Schema(description = "健康文章输入数据传输对象")
    public static class Input {
        @Schema(description = "文章标题")
        private String title;
        
        @Schema(description = "文章内容")
        private String content;
        
        @Schema(description = "作者")
        private String author;
        
        @Schema(description = "发布日期")
        private LocalDate publishDate;
    }

    @Data
    @Schema(description = "健康文章详情数据传输对象")
    public static class Detail {
        @Schema(description = "健康文章ID")
        private Long id;
        
        @Schema(description = "文章标题")
        private String title;
        
        @Schema(description = "文章内容")
        private String content;
        
        @Schema(description = "作者")
        private String author;
        
        @Schema(description = "发布日期")
        private LocalDate publishDate;
        
        @Schema(description = "创建时间")
        private String createTime;
        
        @Schema(description = "更新时间")
        private String updateTime;
    }
} 