// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Utility helper to instantiate spring specific configurations.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Configuration
public class ConfigurationHelper {
  
  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
