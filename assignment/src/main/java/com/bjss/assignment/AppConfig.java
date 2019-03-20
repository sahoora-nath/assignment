package com.bjss.assignment;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class AppConfig {
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:msg");
        messageSource.setDefaultEncoding("UTF-8");
        //refresh cache after every 500 mill-secs
        messageSource.setCacheMillis(500);
        return messageSource;
    }
}
