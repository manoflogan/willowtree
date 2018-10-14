// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.krishnanand.willowtree.GuessGame;
import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.model.UserProfile;
import com.krishnanand.willowtree.repository.QuizRepositoryImplTest.TestConfig;
import com.krishnanand.willowtree.util.UserProfileAnswer;
import com.krishnanand.willowtree.utils.ConfigurationHelper;

/**
 * Unit test for {@link QuizRepositoryImpl}.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= {GuessGame.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class QuizRepositoryImplTest {
  
  @Configuration
  @Import(ConfigurationHelper.class)
  public static class TestConfig {
    
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

  @Autowired
  private QuizRepositoryImpl quizRepository;

  /**
   * Tests initialisation.
   */
  @Test
  public void testRegisterQuiz() throws Exception {
    Quiz quiz = this.quizRepository.registerQuiz();
    Assert.assertNotNull(quiz);
    Assert.assertTrue(quiz.getQuizQuestions().isEmpty());
    Assert.assertNotNull(quiz.getScore());
    Assert.assertEquals(0,  quiz.getScore().getScore());
  }
  
  @Test
  public void testFetchImagesQuestion() throws Exception {
    List<UserProfile> userProfiles = this.quizRepository.fetchImagesQuestion(6);
    Assert.assertFalse(userProfiles.isEmpty());
  }

}
