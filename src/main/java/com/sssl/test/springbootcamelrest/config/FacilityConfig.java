package com.sssl.test.springbootcamelrest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FacilityConfig {

    @Bean
    ObjectMapper objectMapper(){
       return  new ObjectMapper();
    }

}
