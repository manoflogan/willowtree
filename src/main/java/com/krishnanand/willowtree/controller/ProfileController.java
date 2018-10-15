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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.model.ScoreMixin;
import com.krishnanand.willowtree.model.Solution;
import com.krishnanand.willowtree.model.UserAnswer;
import com.krishnanand.willowtree.model.UserProfileQuestion;
import com.krishnanand.willowtree.service.IProfileService;

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
   * Fetches the profile with multiple headshots.
   * 
   * @param quizId unique quiz identifier
   * @return unique question
   */
  @GetMapping(value="/quiz/{quizId}/identifyfromsixjson")
  public ResponseEntity<UserProfileQuestion> fetchUserProfileAndHeadShots(
      @PathVariable("quizId") String quizId, HttpServletRequest request) {
    UserProfileQuestion question = this.profileService.fetchUserProfilesAndHeadShots(
        quizId, RequestContextUtils.getLocale(request));
    LOG.debug("Fetching user profile and headshot for " + quizId);
    if (question == null) {
      return new ResponseEntity<UserProfileQuestion>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<UserProfileQuestion>(question, HttpStatus.OK);
  }

  /**
   * Validates the provided answer with the system answer.
   *
   * @param quizId unique quiz identifier
   * @param questionId unique question identifier
   * @param quizAnswer value object encapsulating the user provided answer
   * @param request http servlet request
   * @return value object encapsulating the solution
   */
  @PostMapping(value="/quiz/{quizId}/question/{questionId}")
  public ResponseEntity<Solution> checkAnswer(
      @PathVariable String quizId, @PathVariable Long questionId,
      @RequestBody UserAnswer quizAnswer, HttpServletRequest request) {
    Solution solution = this.profileService.checkAnswer(
        quizId, questionId, quizAnswer, RequestContextUtils.getLocale(request));
    if (solution == null) {
      return new ResponseEntity<Solution>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<Solution>(solution, HttpStatus.OK);
  }

  /**
   * Fetches the score object by quiz identifier.
   *
   * @param quizId unique quiz identifier
   * @param request http servlet request
   * @return value object encapsulating the score
   */
  @GetMapping(value="/quiz/{quizId}/score")
  public ResponseEntity<ScoreMixin> fetchScoreByQuizId(
      @PathVariable String quizId, HttpServletRequest request) {
    ScoreMixin score =
        this.profileService.fetchScore(quizId, RequestContextUtils.getLocale(request));
    if (score == null) {
      return new ResponseEntity<ScoreMixin>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<ScoreMixin>(score, HttpStatus.OK);
  }
}