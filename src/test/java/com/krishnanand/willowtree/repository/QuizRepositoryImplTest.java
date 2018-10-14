// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.krishnanand.willowtree.GuessGame;
import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.model.UserProfile;

/**
 * Unit test for {@link QuizRepositoryImpl}.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@RunWith(SpringJUnit4ClassRunner.class)
// To load the profiles from a local file.
// @SpringBootTest(classes= {GuessGame.class, TestConfig.class})
@SpringBootTest(classes= {GuessGame.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class QuizRepositoryImplTest {

  @Autowired
  private QuizRepository quizRepository;

  /**
   * Tests initialisation.
   */
  @Test
  public void testRegisterQuiz() throws Exception {
    Quiz quiz = this.quizRepository.registerQuiz();
    Assert.assertNotNull(quiz);
    // Id is intialised.
    Assert.assertNotNull(quiz.getId());
    Assert.assertTrue(quiz.getQuizQuestions().isEmpty());
    Assert.assertNotNull(quiz.getScore());
    Assert.assertEquals(0,  quiz.getScore().getScore());
    Quiz actual = this.quizRepository.findById(quiz.getId()).get();
    Assert.assertEquals(actual, quiz);
  }
  
  @Test
  public void testFetchImagesQuestion() throws Exception {
    List<UserProfile> userProfiles = this.quizRepository.fetchImagesQuestion(6);
    Assert.assertEquals(6, userProfiles.size());
  }
}
