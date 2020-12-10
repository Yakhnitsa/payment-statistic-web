package com.yurets_y.payment_statistic_web.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/login").setViewName("users/login");
    }
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer(){
        return container ->{
               /*Настройка перенаправления в случаи ошибки (если запрашиваемый УРЛ не найден)
               * Насройка для того, чтобы всеми неизвесными запросами занимался Vue-router
               * */
            container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/"));
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/js/** ** ")
//                .addResourceLocations("classpath:/js/")
//                .setCacheControl(CacheControl.maxAge(31, TimeUnit.DAYS));
//        registry.addResourceHandler("/js/** ** ")
//                .addResourceLocations("classpath:/static/")
//                .setCacheControl(CacheControl.maxAge(31, TimeUnit.DAYS));
//        registry.addResourceHandler("/**/** ** ")
//                .addResourceLocations("classpath:/js/")
//                .setCacheControl(CacheControl.maxAge(31, TimeUnit.DAYS));
        registry.addResourceHandler("/**/js/*.js")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS));
    }
}
