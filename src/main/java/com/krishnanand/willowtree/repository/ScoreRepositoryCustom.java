// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import com.krishnanand.willowtree.model.QuizQuestion;
import com.krishnanand.willowtree.model.Solution;
import com.krishnanand.willowtree.model.UserAnswer;

/**
 * Strategy definition for updating custom scores.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
public interface ScoreRepositoryCustom {
  
  /**
   * Updates the score for a quiz.
   * 
   * @param quizQuestion value object representing the asked question and the correct answer
   * @param answer value object representing the user provided answer
   */
  Solution updateScore(QuizQuestion quizQuestion, UserAnswer answer);

}
