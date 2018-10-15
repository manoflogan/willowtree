// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.service;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.krishnanand.willowtree.GuessGame;
import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.model.QuizQuestion;
import com.krishnanand.willowtree.model.ScoreMixin;
import com.krishnanand.willowtree.model.Solution;
import com.krishnanand.willowtree.model.UserAnswer;
import com.krishnanand.willowtree.model.UserProfile;
import com.krishnanand.willowtree.model.UserProfileQuestion;
import com.krishnanand.willowtree.repository.IProfileFetchStatusRepository;
import com.krishnanand.willowtree.repository.IQuizQuestionRepository;
import com.krishnanand.willowtree.repository.IUserProfileRepository;
import com.krishnanand.willowtree.repository.QuizRepository;

/**
 * Unit test for {@link ProfileService}.
 *
 * @author krishnanand (Kartik Krishnanand)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= {GuessGame.class})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProfileServiceTest {

  @Autowired
  private ProfileService profileService;
  
  @Autowired
  private IUserProfileRepository userProfileRepository;
  
  @Autowired
  private IProfileFetchStatusRepository fetchStatusRepository;
  
  @Autowired
  private QuizRepository quizRepository;
  
  @Autowired
  private MessageSource messageSource;
  
  @Autowired
  private IQuizQuestionRepository quizQuestionRepository;
  
  @Before
  public void setUp() throws Exception {
    System.setProperty("user.language", "en");
    System.setProperty("user.country", "US");
  }
  
  @After
  public void tearDown() throws Exception {
    System.clearProperty("user.language");
    System.clearProperty("user.country");
  }
  
  @Test
  public void testInitialiseProfiles() throws Exception {
    this.userProfileRepository.deleteAll();
    this.fetchStatusRepository.deleteAll();
    Assert.assertTrue(this.profileService.initialiseProfiles());
    List<UserProfile> userProfiles = this.userProfileRepository.findAll();
    Assert.assertEquals(userProfiles.size(), 153);
  }
  
  @Test
  public void testRegisterQuiz() throws Exception {
   Quiz actual = this.profileService.registerQuiz();
   Quiz expected = this.quizRepository.findById(actual.getId()).get();
   Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testFetchUserProfilesAndHeadShots() throws Exception {
    Quiz actual = this.profileService.registerQuiz();
    
    UserProfileQuestion userProfileQuestion =
        this.profileService.fetchUserProfilesAndHeadShots(actual.getQuizId(), Locale.getDefault());
    MatcherAssert.assertThat(
        userProfileQuestion.getQuestionText(),
        Matchers.startsWith("Which profile among these is "));
    MatcherAssert.assertThat(userProfileQuestion.getQuizId(), Matchers.equalTo(actual.getQuizId()));
    actual = this.quizRepository.findById(actual.getId()).get();
    Assert.assertEquals(1, actual.getQuizQuestions().size());
    Assert.assertFalse(userProfileQuestion.getImages().isEmpty());
  }

  @Test
  public void testFetchUserProfilesAndHeadShots_Error() throws Exception {
    Quiz actualQuiz = this.profileService.registerQuiz();
    QuizRepository mockQuizRepository = Mockito.mock(QuizRepository.class);
    this.profileService.setQuizRepository(mockQuizRepository);
    Mockito.when(mockQuizRepository.fetchImagesQuestion(Mockito.anyInt())).thenReturn(
        Mockito.eq(Collections.emptyList()));
    UserProfileQuestion actual =
        this.profileService.fetchUserProfilesAndHeadShots(
            actualQuiz.getQuizId(), Locale.getDefault());
    UserProfileQuestion expected = new UserProfileQuestion();
    expected.addError(
        404, this.messageSource.getMessage("user.profile.not.found",
            new Object[] {actualQuiz.getQuizId()},  Locale.getDefault()));
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testCheckAnswer_QuizDoesNotExist() throws Exception {
    String quizId = "invalid";
    Solution actual = this.profileService.checkAnswer(quizId, null, null, Locale.getDefault());
    Solution expected = new Solution();
    expected.addError(404, this.messageSource.getMessage(
        "quiz.not.found", new Object[] {quizId}, Locale.getDefault()));
    Assert.assertEquals(actual, expected);
  }
 
  @Test
  public void testCheckAnswer_QuizAllQuestionsAnswered() throws Exception {
    Quiz quizObject = this.profileService.registerQuiz();
    UserProfileQuestion questions = this.profileService.fetchUserProfilesAndHeadShots(
        quizObject.getQuizId(), Locale.getDefault());
    IQuizQuestionRepository mockQuizQuestionRepository = Mockito.spy(this.quizQuestionRepository);
    this.profileService.setQuizQuestionRepository(mockQuizQuestionRepository);
    Mockito.when(
        mockQuizQuestionRepository.findQuestionTypesByQuizId(quizObject.getQuizId())).thenReturn(
            this.profileService.getAllQuizQuestionTypes());
    Solution actual = this.profileService.checkAnswer(
        quizObject.getQuizId(), questions.getQuestionId(), null, Locale.getDefault());
    Solution expected = new Solution();
    expected.addError(400, this.messageSource.getMessage(
        "quiz.ended", new Object[] {quizObject.getQuizId()}, Locale.getDefault()));
    Assert.assertEquals(actual, expected);
    Mockito.verify(mockQuizQuestionRepository).findQuestionTypesByQuizId(quizObject.getQuizId());
  }
  
  @Test
  public void testCheckAnswer_QuizQuestionNotFound() throws Exception {
    Quiz quizObject = this.profileService.registerQuiz();
    Solution actual = this.profileService.checkAnswer(
        quizObject.getQuizId(), 1000L, null, Locale.getDefault());
    Solution expected = new Solution();
    expected.addError(404, this.messageSource.getMessage(
        "question.not.found", new Object[] {1000L, quizObject.getQuizId()}, Locale.getDefault()));
    Assert.assertEquals(actual, expected);
  }
  
  @Test
  public void testCheckAnswer_QuizQuestionCorrectAnswer() throws Exception {
    Quiz quizObject = this.profileService.registerQuiz();
    Locale locale = Locale.getDefault();
    UserProfileQuestion quizQuestions =
        this.profileService.fetchUserProfilesAndHeadShots(quizObject.getQuizId(), locale);
    QuizQuestion quizQuestion =
        this.quizQuestionRepository.findById(quizQuestions.getQuestionId()).get();
    UserAnswer userAnswer = new UserAnswer();
    userAnswer.setAnswer(quizQuestion.getQuizAnswer().getCorrectAnswer());
    userAnswer.setQuestionId(quizQuestions.getQuestionId());
    userAnswer.setQuizId(quizObject.getQuizId());
    Solution actual = this.profileService.checkAnswer(
        quizObject.getQuizId(), quizQuestions.getQuestionId(), userAnswer, Locale.getDefault());
    Solution expected = new Solution();
    expected.setIsCorrect(Boolean.TRUE);
    expected.setPlayerAnswer(quizQuestion.getQuizAnswer().getCorrectAnswer());
    expected.setQuizId(quizObject.getQuizId());
    expected.setQuestionId(quizQuestion.getId());
    Assert.assertEquals(expected, actual);
    QuizQuestion updatedQuestion =
        this.quizQuestionRepository.findById(quizQuestions.getQuestionId()).get();
    // Checking system updates.
    Assert.assertTrue(updatedQuestion.getAnsweredCorrectly());
    Assert.assertEquals(updatedQuestion.getIncorrectAttempts(), 0);
    Assert.assertEquals(updatedQuestion.getTotalAttempts(), 1);
  }
  
  @Test
  public void testCheckAnswer_QuizQuestionCorrectAnswer_AskedTwice() throws Exception {
    Quiz quizObject = this.profileService.registerQuiz();
    Locale locale = Locale.getDefault();
    UserProfileQuestion quizQuestions =
        this.profileService.fetchUserProfilesAndHeadShots(quizObject.getQuizId(), locale);
    QuizQuestion quizQuestion =
        this.quizQuestionRepository.findById(quizQuestions.getQuestionId()).get();
    UserAnswer userAnswer = new UserAnswer();
    userAnswer.setAnswer(quizQuestion.getQuizAnswer().getCorrectAnswer());
    userAnswer.setQuestionId(quizQuestions.getQuestionId());
    userAnswer.setQuizId(quizObject.getQuizId());
    Solution actual = this.profileService.checkAnswer(
        quizObject.getQuizId(), quizQuestions.getQuestionId(), userAnswer, Locale.getDefault());
    Solution expected = new Solution();
    expected.setIsCorrect(Boolean.TRUE);
    expected.setPlayerAnswer(quizQuestion.getQuizAnswer().getCorrectAnswer());
    expected.setQuizId(quizObject.getQuizId());
    expected.setQuestionId(quizQuestion.getId());
    Assert.assertEquals(expected, actual);
    // Ask again.
    Solution askedAgain = this.profileService.checkAnswer(
        quizObject.getQuizId(), quizQuestions.getQuestionId(), userAnswer, Locale.getDefault());
    Solution error = new Solution();
    error.addError(400, this.messageSource.getMessage(
          "question.already.asked", new Object[] {quizQuestion.getId()}, locale));
    Assert.assertEquals(error, askedAgain);
  }

  @Test
  public void testCheckAnswer_QuizQuestionInCorrectAnswer() throws Exception {
    Quiz quizObject = this.profileService.registerQuiz();
    Locale locale = Locale.getDefault();
    UserProfileQuestion quizQuestions =
        this.profileService.fetchUserProfilesAndHeadShots(quizObject.getQuizId(), locale);
    QuizQuestion quizQuestion =
        this.quizQuestionRepository.findById(quizQuestions.getQuestionId()).get();
    UserAnswer userAnswer = new UserAnswer();
    String answer = quizQuestion.getQuizAnswer().getCorrectAnswer() + "-wrong";
    userAnswer.setAnswer(answer);
    userAnswer.setQuestionId(quizQuestions.getQuestionId());
    userAnswer.setQuizId(quizObject.getQuizId());
    Solution actual = this.profileService.checkAnswer(
        quizObject.getQuizId(), quizQuestions.getQuestionId(), userAnswer, Locale.getDefault());
    Solution expected = new Solution();
    expected.setIsCorrect(Boolean.FALSE);
    expected.setPlayerAnswer(answer);
    expected.setQuizId(quizObject.getQuizId());
    expected.setQuestionId(quizQuestion.getId());
    Assert.assertEquals(expected, actual);
    QuizQuestion updatedQuestion =
        this.quizQuestionRepository.findById(quizQuestions.getQuestionId()).get();
    // Checking system updates.
    Assert.assertFalse(updatedQuestion.getAnsweredCorrectly());
    Assert.assertEquals(updatedQuestion.getIncorrectAttempts(), 1);
    Assert.assertEquals(updatedQuestion.getTotalAttempts(), 1);
  }

  @Test
  public void testCheckAnswer_QuizQuestionInCorrectAnswer_ThenCorrect() throws Exception {
    Quiz quizObject = this.profileService.registerQuiz();
    Locale locale = Locale.getDefault();
    UserProfileQuestion quizQuestions =
        this.profileService.fetchUserProfilesAndHeadShots(quizObject.getQuizId(), locale);
    QuizQuestion quizQuestion =
        this.quizQuestionRepository.findById(quizQuestions.getQuestionId()).get();
    UserAnswer userAnswer = new UserAnswer();
    userAnswer.setAnswer(quizQuestion.getQuizAnswer().getCorrectAnswer() + "-wrong");
    userAnswer.setQuestionId(quizQuestions.getQuestionId());
    userAnswer.setQuizId(quizObject.getQuizId());
    this.profileService.checkAnswer(
        quizObject.getQuizId(), quizQuestions.getQuestionId(), userAnswer, Locale.getDefault());
    // Try again
    userAnswer.setAnswer(quizQuestion.getQuizAnswer().getCorrectAnswer());
    Solution actual = this.profileService.checkAnswer(
        quizObject.getQuizId(), quizQuestions.getQuestionId(), userAnswer, Locale.getDefault());
    
    Solution expected = new Solution();
    expected.setIsCorrect(Boolean.TRUE);
    expected.setPlayerAnswer(quizQuestion.getQuizAnswer().getCorrectAnswer());
    expected.setQuizId(quizObject.getQuizId());
    expected.setQuestionId(quizQuestion.getId());
    Assert.assertEquals(expected, actual);
    QuizQuestion updatedQuestion =
        this.quizQuestionRepository.findById(quizQuestions.getQuestionId()).get();
    // Checking system updates.
    Assert.assertTrue(updatedQuestion.getAnsweredCorrectly());
    Assert.assertEquals(updatedQuestion.getIncorrectAttempts(), 1);
    Assert.assertEquals(updatedQuestion.getTotalAttempts(), 2);
  }
  
  @Test
  public void testFetchScore_CorrectAnswer() throws Exception {
    Quiz quizObject = this.profileService.registerQuiz();
    Locale locale = Locale.getDefault();
    UserProfileQuestion quizQuestions =
        this.profileService.fetchUserProfilesAndHeadShots(quizObject.getQuizId(), locale);
    QuizQuestion quizQuestion =
        this.quizQuestionRepository.findById(quizQuestions.getQuestionId()).get();
    UserAnswer userAnswer = new UserAnswer();
    String answer = quizQuestion.getQuizAnswer().getCorrectAnswer();
    userAnswer.setAnswer(answer);
    userAnswer.setQuestionId(quizQuestions.getQuestionId());
    userAnswer.setQuizId(quizObject.getQuizId());
    this.profileService.checkAnswer(
        quizObject.getQuizId(), quizQuestions.getQuestionId(), userAnswer, Locale.getDefault());
    ScoreMixin actual = this.profileService.fetchScore(quizObject.getQuizId(), locale);
    ScoreMixin expected = new ScoreMixin();
    expected.setQuizId(quizObject.getQuizId());
    expected.setScore(1);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFetchScore_InCorrectAnswer() throws Exception {
    Quiz quizObject = this.profileService.registerQuiz();
    Locale locale = Locale.getDefault();
    UserProfileQuestion quizQuestions =
        this.profileService.fetchUserProfilesAndHeadShots(quizObject.getQuizId(), locale);
    QuizQuestion quizQuestion =
        this.quizQuestionRepository.findById(quizQuestions.getQuestionId()).get();
    UserAnswer userAnswer = new UserAnswer();
    String answer = quizQuestion.getQuizAnswer().getCorrectAnswer() + "-wrong";
    userAnswer.setAnswer(answer);
    userAnswer.setQuestionId(quizQuestions.getQuestionId());
    userAnswer.setQuizId(quizObject.getQuizId());
    this.profileService.checkAnswer(
        quizObject.getQuizId(), quizQuestions.getQuestionId(), userAnswer, Locale.getDefault());
    ScoreMixin actual = this.profileService.fetchScore(quizObject.getQuizId(), locale);
    ScoreMixin expected = new ScoreMixin();
    expected.setQuizId(quizObject.getQuizId());
    expected.setScore(0);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFetchScore_InvalidQuizId() throws Exception {
    Locale locale = Locale.getDefault();
    ScoreMixin actual = this.profileService.fetchScore("missing", locale);
    ScoreMixin expected = new ScoreMixin();
    expected.addError(
        404, this.messageSource.getMessage("quiz.not.found", new Object[] {"missing"}, locale));
    Assert.assertEquals(expected, actual);
  }
}
