package com.ledahl.services.authservice.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebMvc
@Configuration
class WebMvcConfiguration: WebMvcConfigurer {
    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/login").setViewName("login")
        registry.addViewController("/error").setViewName("error")
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/")
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/")
    }
}