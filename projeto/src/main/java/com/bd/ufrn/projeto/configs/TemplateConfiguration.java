package com.bd.ufrn.projeto.configs;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Configuration
public class TemplateConfiguration {

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }




//    @Bean
//    public SpringTemplateEngine templateEngine() {
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        templateEngine.addDialect(new LayoutDialect());
//        return templateEngine;
//    }
}
