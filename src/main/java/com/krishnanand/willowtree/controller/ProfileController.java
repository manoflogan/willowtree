// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.model.UserProfileQuestion;
import com.krishnanand.willowtree.service.IProfileService;
import com.krishnanand.willowtree.utils.QuestionTypes;

/**
 * Instance of this class encapsulates all the profiles of a controller.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@RestController
@RequestMapping("/willowtree")
public class ProfileController {
  
  private static final Log LOG = LogFactory.getLog(ProfileController.class);
  
  private final IProfileService profileService;
  
  @Autowired
  public ProfileController(IProfileService quizService) {
    this.profileService = quizService;
  }
  

  /**
   * Initialises the quiz.
   * 
   * @return registration information
   */
  @PostMapping("/quiz")
  public ResponseEntity<Quiz> initialiseQuiz() {
    Quiz quiz = this.profileService.registerQuiz();
    LOG.info("Initialised the quiz object.");
    return new ResponseEntity<Quiz>(quiz, HttpStatus.CREATED);
  }

  /**
   * Fetches the questions to be answered.
   * 
   * @param quizId unique quiz identifier
   * @return unique question
   */
  @GetMapping(value="/quiz/{quizId}/identifyfromsixjson")
  public ResponseEntity<UserProfileQuestion> fetchUserProfileAndHeadShot(
      @PathVariable("quizId") String quizId, HttpServletRequest request) {
    UserProfileQuestion question = this.profileService.fetchUserProfilesAndHeadShots(
        quizId, QuestionTypes.IDENTIFY_AMONG_SIX_FACE, RequestContextUtils.getLocale(request));
    LOG.debug("Fetching user profile and headshot for " + quizId);
    if (question == null) {
      return new ResponseEntity<UserProfileQuestion>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<UserProfileQuestion>(question, HttpStatus.OK);
  }
  
  
}