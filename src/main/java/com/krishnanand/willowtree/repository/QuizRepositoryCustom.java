// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import com.krishnanand.willowtree.model.Quiz;

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

}
