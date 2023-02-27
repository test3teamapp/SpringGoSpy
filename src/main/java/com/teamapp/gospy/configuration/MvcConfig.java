package com.teamapp.gospy.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/dashboard").setViewName("dashboard");
        registry.addViewController("/").setViewName("/dashboard");
        registry.addViewController("/maps").setViewName("maps");
        //registry.addRedirectViewController("/maps/**", "/maps");
        registry.addViewController("/chat").setViewName("chat");
        registry.addViewController("/login").setViewName("login");
        //registry.addViewController("/error").setViewName("error");
    }

}
