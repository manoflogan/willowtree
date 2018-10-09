// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An instance of this class encapsulates the user provided answer of the quiz question.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class QuizAnswer {
  
  // Quiz Id associated with the question.
  private String quizId;
  
  // Unique profile id that represents the correct answer.
  @JsonProperty("id")
  private String profileId;
  
  // User provided answer.
  @JsonProperty("answer")
  private Boolean quizAnswer;
  
  // Unique question id associated wit hthe question.
  private Long questionId;
}
