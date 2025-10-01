package com.dzk.wx.api.healtharticle;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HealthArticleService extends ServiceImpl<HealthArticleMapper, HealthArticle> {

    @Autowired
    private HealthArticleMapper healthArticleMapper;

    @Autowired
    private HealthArticleConverter healthArticleConverter;

    public List<HealthArticle> getArticlesByAuthor(String author) {
        return healthArticleMapper.getArticlesByAuthor(author);
    }

    public HealthArticle getArticleById(Long id) {
        return healthArticleMapper.getArticleById(id);
    }

    public HealthArticle saveArticle(HealthArticle healthArticle) {
        int result = healthArticleMapper.insert(healthArticle);
        return healthArticleMapper.getArticleById(healthArticle.getId());
    }

    public boolean updateArticle(HealthArticle healthArticle) {
        return healthArticleMapper.updateById(healthArticle) > 0;
    }

    public boolean deleteArticle(Long id) {
        return healthArticleMapper.deleteById(id) > 0;
    }
} 