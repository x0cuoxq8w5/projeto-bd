package com.bd.ufrn.projeto.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/estoque", "/estoque/pedidos");
        registry.addRedirectViewController("/estoque/", "/estoque/pedidos");
        registry.addRedirectViewController("/recepcao", "/recepcao/reservas");
        registry.addRedirectViewController("/recepcao/", "/recepcao/reservas");
    }
}
