package com.brightcoding.app.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	public void addCorsMappings(CorsRegistry registry) {
		//registry.addMapping("/users").allowedMethods("GET", "POST", "PUT").allowedOrigins("http://localhost:4200");
		registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
	}
	
	 /*@Override
	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
		 registry.addResourceHandler("swagger-ui.html")
         .addResourceLocations("classpath:/META-INF/resources/");

		 registry.addResourceHandler("/webjars/**")
		         .addResourceLocations("classpath:/META-INF/resources/webjars/");
	 }*/
}
