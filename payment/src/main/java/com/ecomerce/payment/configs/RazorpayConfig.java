package com.ecomerce.payment.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Configuration
public class RazorpayConfig {
    @Value("${razorpy.key.id}")
     private String key;
     @Value("${razorpy.key.secret}")
     private String secretKey;
    @Bean
    public RazorpayClient getRazorpayClient() throws RazorpayException{
    return new RazorpayClient(key, secretKey);
    }
    
}
