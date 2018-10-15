// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.krishnanand.willowtree.GuessGame;
import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.model.QuizQuestion;
import com.krishnanand.willowtree.model.ScoreMixin;
import com.krishnanand.willowtree.model.Solution;
import com.krishnanand.willowtree.model.UserProfileQuestion;
import com.krishnanand.willowtree.repository.IQuizQuestionRepository;


/**
 * Unit test for {@link ProfileController}.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= {GuessGame.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProfileControllerTest {

  @Autowired
  private ProfileController controller;

  private MockMvc mockMvc;

  @Autowired
  private IQuizQuestionRepository quizQuestionRepository;

  @Before
  public void setUp() throws Exception {
    this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
  }

  @After
  public void tearDown() throws Exception {
    this.mockMvc = null;
  }
  
  private Quiz initQuiz(ObjectMapper mapper) throws Exception {
    MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.post("/willowtree/quiz")).
        andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper.readValue(result.getResponse().getContentAsByteArray(), Quiz.class);
  }
  
  private UserProfileQuestion initQuizQuestion(Quiz quiz, ObjectMapper mapper) throws Exception {
    // Get request.
   MvcResult quizResult = this.mockMvc.perform(MockMvcRequestBuilders.get(
        "/willowtree/quiz/{quizId}/identifyfromsixjson", quiz.getQuizId())).andExpect(
            MockMvcResultMatchers.status().isOk()).andReturn();
   String quizQuestions = quizResult.getResponse().getContentAsString();
   return mapper.readValue(quizQuestions, UserProfileQuestion.class);
  }
  
  private QuizQuestion initQuizQuestion(ObjectMapper mapper) throws Exception {
    Quiz quiz = initQuiz(mapper);
    UserProfileQuestion userProfileQuestion = initQuizQuestion(quiz, mapper);
    QuizQuestion quizQuestion =
        this.quizQuestionRepository.findById(userProfileQuestion.getQuestionId()).get();
    // No questions answered.
    Assert.assertEquals(0, quizQuestion.getTotalAttempts());
    Assert.assertEquals(0, quizQuestion.getIncorrectAttempts());
    return quizQuestion;
  }

  private Solution callCheckAnswer(QuizQuestion quizQuestion, String answer, ObjectMapper mapper,
      boolean correctAnswer) throws Exception {
    // Make a request.
    Map<String, Object> postBody = new LinkedHashMap<>();
    postBody.put("id", answer);
    postBody.put("quizId", quizQuestion.getQuiz().getQuizId());
    postBody.put("questionId", quizQuestion.getId());
    String quizAnswerResponseBody = mapper.writeValueAsString(postBody);
    MvcResult solutionResult =
        this.mockMvc
            .perform(MockMvcRequestBuilders
                .post("/willowtree/quiz/{quizId}/question/{questionId}",
                    quizQuestion.getQuiz().getQuizId(), quizQuestion.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(quizAnswerResponseBody))
            .andReturn();
    Solution actual =
        mapper.readValue(solutionResult.getResponse().getContentAsByteArray(), Solution.class);
    Solution expected = new Solution();
    expected.setIsCorrect(correctAnswer);
    expected.setPlayerAnswer(answer);
    expected.setQuestionId(quizQuestion.getId());
    expected.setQuizId(quizQuestion.getQuiz().getQuizId());
    Assert.assertEquals(expected, actual);
    // Updated quiz question.
    quizQuestion = this.quizQuestionRepository.findById(quizQuestion.getId()).get();
    Assert.assertEquals(1, quizQuestion.getTotalAttempts());
    if (correctAnswer) {
      Assert.assertEquals(0, quizQuestion.getIncorrectAttempts());
    } else {
      Assert.assertEquals(1,  quizQuestion.getIncorrectAttempts());
    }
    return actual;
  }
  
  private void checkScore(QuizQuestion quizQuestion, String answer, ObjectMapper mapper,
      boolean correctAnswer) throws Exception {
    this.callCheckAnswer(quizQuestion, answer, mapper, correctAnswer);
    MvcResult scoreResult =
        this.mockMvc
            .perform(MockMvcRequestBuilders
                .get("/willowtree/quiz/{quizId}/score",quizQuestion.getQuiz().getQuizId()))
            .andReturn();
    ScoreMixin actualScore = mapper.readValue(
        scoreResult.getResponse().getContentAsByteArray(), ScoreMixin.class);
    ScoreMixin expectedScore = new ScoreMixin();
    expectedScore.setQuizId(quizQuestion.getQuiz().getQuizId());
    expectedScore.setScore(correctAnswer ? 1 : 0);
    Assert.assertEquals(expectedScore, actualScore);
  }

  @Test
  public void testInitialiseQuiz() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    Quiz quiz = initQuiz(mapper);
    Assert.assertNotNull(quiz.getQuizId());
  }

  @Test
  public void testFetchProfileAndHeadshots() throws Exception {
   ObjectMapper mapper = new ObjectMapper();
   Quiz quiz = initQuiz(mapper);
   UserProfileQuestion userProfileQuestion = initQuizQuestion(quiz, mapper);
   MatcherAssert.assertThat(userProfileQuestion.getQuestionText(),
       Matchers.startsWith("Which profile among these is"));
   Assert.assertEquals(quiz.getQuizId(), userProfileQuestion.getQuizId());
   Assert.assertTrue(!userProfileQuestion.getImages().isEmpty());
  }

  @Test
  public void testCheckAnswer_correctAnswer() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    QuizQuestion quizQuestion = initQuizQuestion(mapper);
    callCheckAnswer(quizQuestion, quizQuestion.getQuizAnswer().getCorrectAnswer(), mapper, true);
  }

  @Test
  public void testCheckAnswer_inCorrectAnswer() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    QuizQuestion quizQuestion = initQuizQuestion(mapper);
    callCheckAnswer(quizQuestion, "wrong answer", mapper, false);
  }

  @Test
  public void testFetchScoreByQuizId_CorrectAnswer() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    QuizQuestion quizQuestion = initQuizQuestion(mapper);
    this.checkScore(quizQuestion, quizQuestion.getQuizAnswer().getCorrectAnswer(), mapper, true);
  }

  @Test
  public void testFetchScoreByQuizId_IncorrectAnswer() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    QuizQuestion quizQuestion = initQuizQuestion(mapper);
    this.checkScore(quizQuestion, "wrong", mapper, false);
  }
}
