package com.ecommerce.product.configures;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.product.services.DBProductService;
import com.ecommerce.product.services.ProductService;

@Configuration
public class ApllicationConfigure {
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
