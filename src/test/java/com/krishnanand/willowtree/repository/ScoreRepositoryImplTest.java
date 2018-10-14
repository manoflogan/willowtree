// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

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
import com.krishnanand.willowtree.model.QuizAnswer;
import com.krishnanand.willowtree.model.QuizQuestion;
import com.krishnanand.willowtree.model.Score;
import com.krishnanand.willowtree.model.Solution;
import com.krishnanand.willowtree.model.UserAnswer;
import com.krishnanand.willowtree.utils.QuestionType;

/**
 * Unit test for {@link ScoreRepositoryImpl}.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@RunWith(SpringJUnit4ClassRunner.class)
//To load the profiles from a local file.
//@SpringBootTest(classes= {GuessGame.class, TestConfig.class})
@SpringBootTest(classes= {GuessGame.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ScoreRepositoryImplTest {
  
  @Autowired
  private ScoreRepository scoreRepository;
  
  @Autowired
  private IQuizQuestionRepository quizQuestionRepository;
  
  @Autowired
  private QuizRepository quizRepository;
  
  private Quiz quizObject;
  
  private QuizQuestion quizQuestion;
  
  public void setUp(String answerId) throws Exception {
    quizObject = quizRepository.registerQuiz();
    // Init quiz question.
     quizQuestion = new QuizQuestion();
    quizQuestion.setQuiz(quizObject);
    quizQuestion.setQuestionType(QuestionType.PROFILE_FROM_HEAD_SHOTS);
    quizObject.getQuizQuestions().add(quizQuestion);
    
    // Initialising quiz answer.
    QuizAnswer quizAnswer = new QuizAnswer();
    quizAnswer.setCorrectAnswer(answerId);
    quizQuestion.setQuizAnswer(quizAnswer);
    quizAnswer.setQuizQuestion(quizQuestion);
    this.quizQuestionRepository.save(quizQuestion);
    Assert.assertFalse(quizQuestion.getAnsweredCorrectly());
    Assert.assertEquals(quizQuestion.getIncorrectAttempts(), 0);
    Assert.assertEquals(quizQuestion.getTotalAttempts(), 0);
  }
  
  @Test
  public void testUpdateScore() throws Exception {
    String answerId = "answer";
    this.setUp(answerId);
    
    UserAnswer userAnswer = new UserAnswer();
    userAnswer.setQuestionId(quizQuestion.getId());
    userAnswer.setAnswer(answerId);
    userAnswer.setQuizId(quizObject.getQuizId());
    Solution actual = this.scoreRepository.updateScore(quizQuestion, userAnswer);
    this.quizQuestionRepository.save(quizQuestion);
    Solution expected = new Solution();
    expected.setIsCorrect(true);
    expected.setPlayerAnswer(answerId);
    expected.setQuestionId(quizQuestion.getId());
    expected.setQuizId(quizObject.getQuizId());
    Assert.assertEquals(expected, actual);
    
    // Assertions.
    QuizQuestion answeredQuizQuestion =
        this.quizQuestionRepository.findById(quizQuestion.getId()).get();
    Assert.assertEquals(1, answeredQuizQuestion.getTotalAttempts());
    Assert.assertEquals(0, answeredQuizQuestion.getIncorrectAttempts());
    Assert.assertTrue(answeredQuizQuestion.getAnsweredCorrectly());
    
    Score actualScore = this.scoreRepository.findById(quizObject.getId()).get();
    Score expectedScore = new Score();
    expectedScore.setId(quizObject.getId());
    expectedScore.setScore(1);
    Assert.assertEquals(expectedScore, actualScore);
  }
}
