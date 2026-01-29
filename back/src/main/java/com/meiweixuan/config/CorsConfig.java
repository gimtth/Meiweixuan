package com.meiweixuan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许跨域的源
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("http://localhost:5174");
        
        // 允许跨域的头信息
        config.addAllowedHeader("*");
        
        // 允许跨域的方法
        config.addAllowedMethod("*");
        
        // 允许携带凭证信息（cookies等）
        config.setAllowCredentials(true);
        
        // 暴露响应头
        config.addExposedHeader("Authorization");
        
        // 预检请求的缓存时间（秒）
        config.setMaxAge(3600L);
        
        // 对所有URL应用CORS配置
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
} 