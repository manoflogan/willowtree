// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * An instance of this class encapsulates the serialised user response to the question. 
 * 
 * <p>The sample JSON response would as given below: <pre class="code">
 * {
     "id": <answer to the quiz>
     "quizId": <unique quiz identifier>
     "questionId": <unique question identifier>
   }
 * </pre>
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
public class UserAnswer implements Serializable {
  
  @JsonProperty("id")
  private String answer;
  
  private String quizId;
  
  private Long questionId;
}
