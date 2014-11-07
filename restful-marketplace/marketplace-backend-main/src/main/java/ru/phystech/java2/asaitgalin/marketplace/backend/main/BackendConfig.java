package ru.phystech.java2.asaitgalin.marketplace.backend.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@EnableAutoConfiguration
@ComponentScan("ru.phystech.java2.asaitgalin.marketplace")
@PropertySource(value = "file:marketplace.properties", ignoreResourceNotFound = true)
public class BackendConfig {
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendConfig.class, args);
    }
}
