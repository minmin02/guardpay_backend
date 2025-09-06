package com.example.guardpay.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry r) {
        r.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:5173", "http://127.0.0.1:5173", // Flutter 웹 디버깅용(선택)
                        "http://10.0.2.2:5173",                           // 에뮬레이터 웹(선택)
                        "*"                                                // 앱에서는 CORS 영향 적지만 초기엔 넉넉히
                )
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}