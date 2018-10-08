// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.utils;

import lombok.ToString;

/**
 * An instance of this class encapulates the types of questions asked.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@ToString
public enum QuestionTypes {
  
  IDENTIFY_AMONG_SIX_FACE("identity_among_six_faces");
  
  private final String type;
  
  QuestionTypes(String type) {
    this.type = type;
  }
  
  public String getType() {
    return this.type;
  }
}
