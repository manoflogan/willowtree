// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.service;

import java.util.Locale;

import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.model.Score;
import com.krishnanand.willowtree.model.Solution;
import com.krishnanand.willowtree.model.UserAnswer;
import com.krishnanand.willowtree.model.UserProfileQuestion;

/**
 * An instance of this class encapsulates all the functions related to profiles.
 * @author krishnanand (Kartik Krishnanand)
 */
public interface IProfileService {
  

  /**
   * Fetches the profiles to persist them in the repository.
   */
  boolean initialiseProfiles();
  
  /**
   * Registers and returns a quiz.
   */
  Quiz registerQuiz();
  
  /**
   * Fetch user profiles and headshots for a question to be answered.
   * 
   * @param quizId unique quiz identifier
   * @param locale locale object
   */
  UserProfileQuestion fetchUserProfilesAndHeadShots(
      String quizId, Locale locale);
  
  /**
   * Verifies the user answer with the one on the record.
   * 
   * @param quizId unique quiz identifier
   * @param questionId unique question identifier
   * @param answer value object representing the user provided answer
   * @param locale locale object
   */
  Solution checkAnswer(String quizId, Long questionId, UserAnswer answer, Locale locale);
  
  /**
   * Fetches the score by quiz id.
   * 
   * @param quizId unique quiz identifier
   * @param locale locale object
   * @return score representation of quiz
   */
  Score fetchScore(String quizId, Locale locale);

}
