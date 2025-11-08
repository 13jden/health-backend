package com.dzk.wx.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Value("${upload.commonPath}")
    private String commonPath;

    @Value("${file.diet.path}")
    private String dietPath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射通用文件路径
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + commonPath);
        
        // 映射饮食图片路径（如果dietPath不在commonPath下）
        // 确保路径以/结尾
        String dietPathWithSlash = dietPath.endsWith("/") ? dietPath : dietPath + "/";
        registry.addResourceHandler("/files/diet/**")
                .addResourceLocations("file:" + dietPathWithSlash);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")   // 注意：Spring Boot 2.4+ 推荐用 allowedOriginPatterns
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(mvcTaskExecutor());
        configurer.setDefaultTimeout(30000); // 30秒超时
    }

    @Bean
    public ThreadPoolTaskExecutor mvcTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }

} 