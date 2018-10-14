// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.utils;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

/**
 * An instance of this class overrides production configuration with test specific configuration. To
 * override the specific configuration:
 * <pre class="code">
    @SpringBootTest(classes= {GuessGame.class, TestConfig.class, . . . })
    public class MyTest{
    }
   </pre>
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Configuration
@Import(ConfigurationHelper.class)
public class TestConfig {
  
  @Bean
  @SuppressWarnings("unchecked")
  RestTemplate restTemplate() {
    RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    UserProfileAnswer answer = new UserProfileAnswer();
    answer.addUrlToFileMapping("https://www.willowtreeapps.com/api/v1.0/profiles", "profiles.json");
    Mockito.when(restTemplate.execute(
        Mockito.anyString(), Mockito.eq(HttpMethod.GET), (RequestCallback) Mockito.isNull(),
        Mockito.isA(ResponseExtractor.class), (Object) Mockito.any())).thenAnswer(answer);
    return restTemplate;
  }
}
