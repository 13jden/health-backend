package com.dzk.wx.api.healtharticle;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HealthArticleMapper extends BaseMapper<HealthArticle> {
    
    @Select("SELECT * FROM health_article WHERE author = #{author}")
    List<HealthArticle> getArticlesByAuthor(@Param("author") String author);
    
    @Select("SELECT * FROM health_article WHERE id = #{id}")
    HealthArticle getArticleById(@Param("id") Long id);
} 