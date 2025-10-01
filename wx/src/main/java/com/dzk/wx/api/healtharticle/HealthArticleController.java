package com.dzk.wx.api.healtharticle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("health-article")
public class HealthArticleController {

    @Autowired
    private HealthArticleService healthArticleService;

    @Autowired
    private HealthArticleConverter healthArticleConverter;

} 