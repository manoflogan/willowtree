// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.utils;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Utility helper to instantiate spring specific configurations.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Component
public class ConfigurationHelper {
  
  @Autowired
  private EntityManagerFactory entityManagerFactory;
  
  @Bean
  RestTemplate restTemplate() {
    return new RestTemplate();
  }
  
  @Bean("transactionManager")
  JpaTransactionManager getTransactionManager() {
    return new JpaTransactionManager(entityManagerFactory);
  }
  
  @Bean
  public MessageSource messageSource() {
      ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
      messageSource.setBasename("classpath:messages");
      messageSource.setUseCodeAsDefaultMessage(true);
      messageSource.setCacheSeconds(5);
      messageSource.setDefaultEncoding("UTF-8");
      return messageSource;
  }
}
