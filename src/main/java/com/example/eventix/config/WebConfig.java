package com.example.eventix.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations("file:C:/eventix/uploads/profile-photos/")
                .addResourceLocations("file:src/main/resources/static/uploads/")
                .setCachePeriod(3600);  // Optional: Set cache period for static resources

        registry.addResourceHandler("/clubs/**")
                .addResourceLocations("file:src/main/resources/static/clubs/")
                .setCachePeriod(3600);  // Optional: Set cache period for static resources

    }
}

