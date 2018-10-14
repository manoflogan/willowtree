// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.assertj.core.util.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.krishnanand.willowtree.model.HeadShot;
import com.krishnanand.willowtree.model.ProfileFetchStatus;
import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.model.QuizAnswer;
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
import com.krishnanand.willowtree.repository.ScoreRepository;
import com.krishnanand.willowtree.utils.QuestionType;

/**
 * A single point of entry to all the profile related services.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Service
public class ProfileService implements IProfileService {
  
  private static final Log LOG = LogFactory.getLog(ProfileService.class);

  @Autowired
  private RestTemplate restTemplate;

  @Value("${init.url}")
  private String url;
  
  @Value("${image.picture.count}")
  private int imagePictureCount;

  private final IProfileFetchStatusRepository fetchStatusRepository;
  
  private final IUserProfileRepository userProfileRepository;
  
  @Autowired
  private QuizRepository quizRepository;
  
  @Autowired
  private MessageSource messageSource;
  
  @Autowired
  private IQuizQuestionRepository quizQuestionRepository;
  
  @Autowired
  private ScoreRepository scoreRepository;

  @Autowired
  ProfileService(IProfileFetchStatusRepository fetchStatusRepository,
      IUserProfileRepository userProfileRepository) {
    this.fetchStatusRepository = fetchStatusRepository;
    this.userProfileRepository = userProfileRepository;
  }
  
  @VisibleForTesting
  public void setQuizQuestionRepository(IQuizQuestionRepository quizQuestionRepository) {
    this.quizQuestionRepository = quizQuestionRepository;
  }

  /**
   * Initiates a one time initialisation of user profiles to persist them in the database.
   *
   * <p>Implementation uses a profile fetch status to verify if the profiles have been
   * intialised. If they have not been intialised, then a HTTP request is made to fetch,
   * deserialise, and persist the data.
   *
   * <p><em>IMPORTANT</em>: To ensure that the profiles are initialised only once, it is
   *  recommended that the initialisation process be invoked in a module that is invoked only
   *  once, such as application listeners.
   */
  @Override
  @Transactional
  public boolean initialiseProfiles() {
    ProfileFetchStatus profileFetchStatus =
        this.fetchStatusRepository.findByFetched(Boolean.TRUE);
    if (profileFetchStatus == null || profileFetchStatus.getFetched() == null
        || !profileFetchStatus.getFetched().booleanValue()) {
      LOG.debug("Initialising the user profiles.");
      List<UserProfile> userProfiles =
          this.restTemplate.execute(this.url, HttpMethod.GET, null,
              new ResponseExtractor<List<UserProfile>>() {

                @Override
                public List<UserProfile> extractData(ClientHttpResponse response)
                    throws IOException {
                  try (InputStream is = response.getBody();) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    SimpleModule module = new SimpleModule();
                    module.addDeserializer(List.class, new UserProfileDeserialiser());
                    objectMapper.registerModule(module);
                    return objectMapper.readValue(
                        is, new TypeReference<List<UserProfile>>() {});
                  }
                }

              });
      this.userProfileRepository.saveAll(userProfiles);
      ProfileFetchStatus status = new ProfileFetchStatus();
      status.setFetched(Boolean.TRUE);
      status.setCreationTimeStamp(LocalDateTime.now(ZoneOffset.UTC));
      status.setLastModifiedTimeStamp(LocalDateTime.now(ZoneOffset.UTC));
      this.fetchStatusRepository.save(status);
      LOG.info("Profile initialisation complete");
    }
    return true;
  }

  /**
   * Registers a quiz.
   */
  @Override
  @Transactional
  public Quiz registerQuiz() {
    return this.quizRepository.registerQuiz();
  }

  /**
   * Fetches profile and head shots of questions.
   *
   * <p>In addition, the implementation records the type of questions asked for the quiz.
   */
  @Override
  @Transactional
  public UserProfileQuestion fetchUserProfilesAndHeadShots(
      String quizId, Locale locale) {
    LOG.info("Fetching the user profiles and headshots for quiz id: " + quizId);
    List<UserProfile> userProfiles =
        this.quizRepository.fetchImagesQuestion(this.imagePictureCount);
    if (userProfiles == null || userProfiles.isEmpty()) {
      return null;
    }
    Collections.shuffle(userProfiles);
    UserProfileQuestion userProfileQuestion = new UserProfileQuestion();
    // Choose the first user profile.
    int num = new SecureRandom().nextInt(this.imagePictureCount);
    UserProfile first = userProfiles.get(num);
    userProfileQuestion.setQuizId(quizId);
    QuizQuestion quizQuestion =
        this.initialiseQuizQuestion(quizId, QuestionType.PROFILE_FROM_HEAD_SHOTS,
            first.getHeadshot().getHeadshotId());
    userProfileQuestion.setQuestionId(quizQuestion.getId());
    // Choose random name.
    this.setQuestionText(userProfileQuestion, locale, first.getFirstName(), first.getLastName());
    for (UserProfile userProfile : userProfiles) {
      HeadShot headshot = userProfile.getHeadshot();
      userProfileQuestion.addImage(
          headshot.getUrl(), headshot.getHeight(), headshot.getWidth(),
          headshot.getHeadshotId());
    }
    LOG.info("Fetching the user profiles and headshots completed for quiz id :" + quizId);
    return userProfileQuestion;
  }
  
  /**
   * Registers the questions associated with a quiz.
   *
   * @param quizId unique quiz id
   * @param questionType question type
   * @param answer id correct identifier used to compare against;
   */
  private QuizQuestion initialiseQuizQuestion(String quizId, QuestionType questionType,
      String answerId) {
    Quiz quizObject = this.quizRepository.findByQuizId(quizId);
    
    QuizQuestion quizQuestion = new QuizQuestion();
    quizQuestion.setQuiz(quizObject);
    quizQuestion.setQuestionType(questionType);
    quizObject.getQuizQuestions().add(quizQuestion);
    
    // Initialising quiz answer.
    QuizAnswer quizAnswer = new QuizAnswer();
    quizAnswer.setCorrectAnswer(answerId);
    quizQuestion.setQuizAnswer(quizAnswer);
    quizAnswer.setQuizQuestion(quizQuestion);
    
    return this.quizQuestionRepository.save(quizQuestion);
  }
  
  /**
   * Sets the question text based on locale.
   *
   * @param userProfileQuestion user profile
   * @param questionType enum representing the question type
   * @param locale locale
   * @param args varargs used to populate the resource bundle
   */
  private void setQuestionText(UserProfileQuestion userProfileQuestion,
      Locale locale, Object...args) {
      userProfileQuestion.setQuestionText(
          messageSource.getMessage("identify.person", args, locale));
  }

  /**
   * Checks the solution against the data sets.
   *
   * <p>The implementation checks for the following:
   * <ul>
   *   <li>Verify that the quiz game exists. If not, then an error is returned.</li>
   *   <li>Verify if the question exists. If not, the the error is returned.</li>
   *   <li>If the game exists, and the question has been registered, the solution is checked against
   *   the table based on question type.</li>
   *   <li>The answer is either marked as correct, or incorrect.</li>
   *   <li>Mark the question as answered.</li>
   * </ul>
   * @param answer answer representing the user response
   */
  @Override
  @Transactional
  public Solution checkAnswer(
      String quizId, Long questionId, UserAnswer answer, Locale locale) {
    // Check if the quiz exists.
    Quiz quiz = this.quizRepository.findByQuizId(quizId);
    if (quiz == null) {
      Solution error = new Solution();
      error.addError(400, this.messageSource.getMessage(
          "quiz.not.found", new Object[] {quizId}, locale));
      return error;
    }
    List<String> allQuestions = getAllQuizQuestionTypes();
    List<String> askedQuestionTypes =
        this.quizQuestionRepository.findQuestionTypesByQuizId(quiz.getQuizId());

    if (allQuestions.equals(askedQuestionTypes)) {
      Solution error = new Solution();
      error.addError(400,
          this.messageSource.getMessage("quiz.ended", new Object[] {quizId}, locale));
    }

    // If the question has been asked.
    Optional<QuizQuestion> optionalQuizQuestion =
        this.quizQuestionRepository.findById(questionId);
    if(!optionalQuizQuestion.isPresent()) {
      Solution error = new Solution();
      error.addError(400, this.messageSource.getMessage(
          "question.not.found", new Object[] {questionId, quizId}, locale));
      return error;
    }

    // If the question has been previous answered.
    QuizQuestion quizQuestion = optionalQuizQuestion.get();
    if (quizQuestion.getAnsweredCorrectly() != null &&
        quizQuestion.getAnsweredCorrectly().booleanValue()) {
      Solution error = new Solution();
      error.addError(400, this.messageSource.getMessage(
          "question.already.asked", new Object[] {questionId}, locale));
      return error;
    }
    // Update the score.
    
    Solution solution = this.scoreRepository.updateScore(quizQuestion, answer);
    // Saved by reference
    this.quizQuestionRepository.save(quizQuestion);
    return solution;
  }
  
  /**
   * Returns a list of quiz question types.
   */
  @VisibleForTesting
   List<String> getAllQuizQuestionTypes() {
    List<String> allQuestions = new ArrayList<>();
    QuestionType[] questionTypes = QuestionType.values();
    for (QuestionType questionType : questionTypes) {
      allQuestions.add(questionType.name());
    }
    return allQuestions;
  }

  /**
   * Return the score by quiz id.
   */
  @Override
  @Transactional
  public ScoreMixin fetchScore(String quizId, Locale locale) {
    Quiz quizObject = this.quizRepository.findByQuizId(quizId);
    ScoreMixin scoreMixin = new ScoreMixin();
    if (quizObject == null) {
      scoreMixin.addError(400,
          this.messageSource.getMessage("quiz.not.found", new Object[] {quizId}, locale));
    } else {
      scoreMixin.setQuizId(quizObject.getQuizId());
      scoreMixin.setScore(quizObject.getScore().getScore());
    }
    return scoreMixin;
  }
}
