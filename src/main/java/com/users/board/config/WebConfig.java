package com.users.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 설정 클래스 지정 어노테이션
public class WebConfig implements WebMvcConfigurer {

    private String resourcePath = "/upload/**";
    // view에서 접근할 경로

    private String savePath = "file:///C://Users/cropr/Desktop/Spring/board/src/main/resources/imgs/";
    // 실제 파일 저장 경로

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }
}
