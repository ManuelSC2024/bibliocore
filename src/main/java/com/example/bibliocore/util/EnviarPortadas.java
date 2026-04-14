package com.example.bibliocore.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EnviarPortadas implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(
            org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry registry) {
        // Configura el manejador de recursos para enviar las portadas desde la carpeta portadas
        registry.addResourceHandler("/portadas/**")
                        .addResourceLocations("classpath:/portadas/");
    }
}
