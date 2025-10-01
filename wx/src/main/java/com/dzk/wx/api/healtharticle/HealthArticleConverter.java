package com.dzk.wx.api.healtharticle;

import org.springframework.stereotype.Component;

@Component
public class HealthArticleConverter {
    
    public HealthArticleDto toDto(HealthArticle healthArticle) {
        if (healthArticle == null) {
            return null;
        }
        
        HealthArticleDto dto = new HealthArticleDto();
        dto.setId(healthArticle.getId());
        dto.setTitle(healthArticle.getTitle());
        dto.setContent(healthArticle.getContent());
        dto.setAuthor(healthArticle.getAuthor());
        dto.setPublishDate(healthArticle.getPublishDate());
        return dto;
    }
    
    public HealthArticle toEntity(HealthArticleDto.Input input) {
        if (input == null) {
            return null;
        }
        
        HealthArticle healthArticle = new HealthArticle();
        healthArticle.setTitle(input.getTitle());
        healthArticle.setContent(input.getContent());
        healthArticle.setAuthor(input.getAuthor());
        healthArticle.setPublishDate(input.getPublishDate());
        return healthArticle;
    }

    public HealthArticleDto.Detail toDetail(HealthArticle healthArticle) {
        if (healthArticle == null) {
            return null;
        }
        
        HealthArticleDto.Detail detail = new HealthArticleDto.Detail();
        detail.setId(healthArticle.getId());
        detail.setTitle(healthArticle.getTitle());
        detail.setContent(healthArticle.getContent());
        detail.setAuthor(healthArticle.getAuthor());
        detail.setPublishDate(healthArticle.getPublishDate());
        detail.setCreateTime(healthArticle.getCreateTime() != null ? healthArticle.getCreateTime().toString() : null);
        detail.setUpdateTime(healthArticle.getUpdateTime() != null ? healthArticle.getUpdateTime().toString() : null);
        return detail;
    }
} 