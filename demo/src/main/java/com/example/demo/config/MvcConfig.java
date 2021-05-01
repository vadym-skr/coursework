package com.example.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//це для того, щоб вказати що клас може бути використаний контейнером Spring IoC як конфігураційний клас для бінов.
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    //Допомагає з реєстрацією простих автоматизованих контролерів, попередньо налаштованих з кодом стану і / або поданням.
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // addViewController-  Зіставте URL-шлях або шаблон з контролером уявлення, щоб відобразити відповідь з налаштованим кодом стану і поданням.
        registry.addViewController("/login").setViewName("login");
    }
}
