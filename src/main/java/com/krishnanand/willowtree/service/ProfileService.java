// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.krishnanand.willowtree.model.UserProfile;
import com.krishnanand.willowtree.model.UserProfileQuestion;
import com.krishnanand.willowtree.repository.IProfileFetchStatusRepository;
import com.krishnanand.willowtree.repository.IUserProfileRepository;
import com.krishnanand.willowtree.repository.QuizRepositoryCustom;
import com.krishnanand.willowtree.utils.QuestionTypes;

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
  private QuizRepositoryCustom quizRepository;
  
  @Autowired
  private MessageSource messageSource;

  @Autowired
  ProfileService(IProfileFetchStatusRepository fetchStatusRepository,
      IUserProfileRepository userProfileRepository) {
    this.fetchStatusRepository = fetchStatusRepository;
    this.userProfileRepository = userProfileRepository;
  }

  /**
   * Initiates a one time initialisation of user profiles to persist them in the database.
   * 
   * <p>Implementation uses a profile fetch status to verify if the profiles have been intialised.
   * If they have not been intialised, then a HTTP request is made to fetch, deserialise, and 
   * persist the data.
   * 
   * <p><em>IMPORTANT</em>: To ensure that the profiles are initialised only once, it is recommended
   * that the initialisation process be invoked in a module that is invoked only once, such as
   * application listeners.
   */
  @Override
  @Transactional
  public boolean initialiseProfiles() {
    ProfileFetchStatus profileFetchStatus = this.fetchStatusRepository.findByFetched(Boolean.TRUE);
    if (profileFetchStatus == null || profileFetchStatus.getFetched() == null
        || !profileFetchStatus.getFetched().booleanValue()) {
      LOG.debug("Initialising the user profiles.");
      List<UserProfile> userProfiles = this.restTemplate.execute(this.url, HttpMethod.GET, null,
          new ResponseExtractor<List<UserProfile>>() {

            @Override
            public List<UserProfile> extractData(ClientHttpResponse response) throws IOException {
              try (InputStream is = response.getBody();) {
                ObjectMapper objectMapper = new ObjectMapper();
                SimpleModule module = new SimpleModule();
                module.addDeserializer(List.class, new UserProfileDeserialiser());
                objectMapper.registerModule(module);
                return objectMapper.readValue(is, new TypeReference<List<UserProfile>>() {});
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

  @Override
  @Transactional
  public Quiz registerQuiz() {
    return this.quizRepository.registerQuiz();
  }

  @Override
  @Transactional
  public UserProfileQuestion fetchUserProfilesAndHeadShots(
      String quizId, QuestionTypes questionTypes, Locale locale) {
    LOG.info("Fetching the user profiles and headshots for quiz id: " + quizId);
    List<UserProfile> userProfiles =
        this.quizRepository.fetchImagesQuestion(this.imagePictureCount);
    if (userProfiles == null || userProfiles.isEmpty()) {
      return null;
    }
    Collections.shuffle(userProfiles);
    UserProfileQuestion userProfileQuestion = new UserProfileQuestion();
    // Choose the first user profile.
    UserProfile first = userProfiles.get(0);
    userProfileQuestion.setProfileId(first.getProfileId());
    userProfileQuestion.setQuizId(quizId);
    this.setQuestionText(userProfileQuestion, QuestionTypes.IDENTIFY_AMONG_SIX_FACE, locale);
    for (UserProfile userProfile : userProfiles) {
      HeadShot headshot = userProfile.getHeadshot();
      userProfileQuestion.addImage(headshot.getUrl(), headshot.getHeight(), headshot.getWidth());
    }
    LOG.info("Fetching the user profiles and headshots completed for quiz id :" + quizId);
    return userProfileQuestion;
  }
  
  private void setQuestionText(UserProfileQuestion userProfileQuestion, QuestionTypes questionType,
      Locale locale) {
    if (questionType == QuestionTypes.IDENTIFY_AMONG_SIX_FACE) {
      userProfileQuestion.setQuestionText(messageSource.getMessage("identify.person", null, locale));
    }
  }
}
