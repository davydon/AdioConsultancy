package com.management.config;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Lukman.Arogundade on 12/17/2015.
 */
@Configuration
public class CustomWebMvcAutoConfig extends WebMvcConfigurerAdapter {

    @Value("${external-resource-location}")
    String externalResourceLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        if(!this.externalResourceLocation.endsWith("/")){
            this.externalResourceLocation += "/";
        }

        registry.addResourceHandler("/pub/**").addResourceLocations("file:///" + this.externalResourceLocation);
        super.addResourceHandlers(registry);
    }

  /*  @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("forward:/public/admin/index.html");
        super.addViewControllers(registry);
    }*/


    @Override
    public void addCorsMappings(CorsRegistry registry) {

             registry.addMapping("/**")
               // .allowedOrigins("http://localhost:8080/", "http://localhost:8080")
               .allowedOrigins("*")
                .allowedMethods("POST", "GET",  "PUT", "DELETE")
                .allowedHeaders("*")
                //.allowedHeaders("X-Auth-Token", "Content-Type")
                //.exposedHeaders("custom-header1", "custom-header2")
                .allowCredentials(false)
                .maxAge(4800);
    }
}
