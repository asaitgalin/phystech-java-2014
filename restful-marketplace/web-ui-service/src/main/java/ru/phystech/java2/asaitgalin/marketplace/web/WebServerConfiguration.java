package ru.phystech.java2.asaitgalin.marketplace.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.resourceresolver.SpringResourceResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
@EnableWebMvc
public class WebServerConfiguration extends WebMvcConfigurerAdapter {
    @Value("${ru.phystech.java2.asaigalin.marketplace.web.port:42000}")
    private int port;

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.setPort(port);
        return factory;
    }

    @Bean
    @Autowired
    public SpringResourceResourceResolver resourceResolver() {
        return new SpringResourceResourceResolver();
    }

    @Bean
    public TemplateResolver defaultTemplateResolver() {
        TemplateResolver templateResolver = new TemplateResolver();
        templateResolver.setPrefix("classpath:/html/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setResourceResolver(resourceResolver());
        return templateResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/html/");
    }
}
