// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author krishnanand (Kartik Krishnanand)
 */
@Configuration
public class ConfigurationHelper {
  
  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

}
