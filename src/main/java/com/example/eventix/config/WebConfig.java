package com.example.eventix.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://eventix-18.netlify.app") // Adjust for your frontend URL
                .allowedMethods("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/clubs/**")
                .addResourceLocations("classpath:/static/clubs/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

        registry.addResourceHandler("/qr-codes/**")
                .addResourceLocations("classpath:/static/qr-codes/");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("classpath:/static/uploads/");



//        registry.addResourceHandler("/uploads/**")
////                .addResourceLocations("file:C:/eventix/uploads/profile-photos/")
//                .addResourceLocations("file:src/main/resources/static/uploads/")
//                .setCachePeriod(3600);  // Optional: Set cache period for static resources
//
//        registry.addResourceHandler("/clubs/**")
//                .addResourceLocations("file:src/main/resources/static/clubs/")
//                .setCachePeriod(3600);  // Optional: Set cache period for static resources

    }
}

