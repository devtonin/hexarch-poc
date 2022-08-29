package com.sensedia.opin.hexarch.application.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebInterceptorsConfiguration implements WebMvcConfigurer {
   @Autowired
   LogInterceptorConfiguration logInterceptorConfiguration;

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(logInterceptorConfiguration).addPathPatterns("/**");
   }
}
