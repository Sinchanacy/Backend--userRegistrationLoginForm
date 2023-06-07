package com.example.JWTSpringBoot.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                /*
                registry.addMapping("/welcome/**")
                        .allowedOrigins("http://localhost:3000") // Update with the URL of your frontend application
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
                registry.addMapping("/hello")
                        .allowedOrigins("http://localhost:3000") // Update with the URL of your frontend application
                        .allowedMethods("GET")
                        .allowedHeaders("*");
                registry.addMapping("/resetpassword")
                        .allowedOrigins("http://localhost:3000") // Update with the URL of your frontend application
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");
                registry.addMapping("/savePassword")
                        .allowedOrigins("http://localhost:3000") // Update with the URL of your frontend application
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*");

                 */
            }


        };

    }
}