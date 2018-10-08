// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import java.util.List;

import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.model.UserProfile;

/**
 * Strategy implementation of IQuiz Repository;
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
public interface QuizRepositoryCustom  {
  
  /**
   * An implementation of this function registers a quiz entry.
   * 
   * @return initialised quiz
   */
  Quiz registerQuiz();
  
  List<UserProfile> fetchImagesQuestion(int count);
}
