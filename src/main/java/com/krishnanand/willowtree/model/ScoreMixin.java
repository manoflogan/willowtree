// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An instance of this class represents the serialised score.
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@ToString
@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
public class ScoreMixin extends IError {
  
  private int score;
  
  private String quizId;
  

}
