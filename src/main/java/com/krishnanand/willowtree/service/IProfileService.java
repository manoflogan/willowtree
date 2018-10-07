// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.service;

import com.krishnanand.willowtree.model.Quiz;

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
   * Registers a quiz.
   * 
   * <p>Quiz identifier will be used to identify the score.
   */
  Quiz registerQuiz();

}
