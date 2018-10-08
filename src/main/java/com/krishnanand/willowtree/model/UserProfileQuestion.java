// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Encapsulate the asked questions.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class UserProfileQuestion {
  
  /**
   * An instance of this class is used to represent the image used to ask a question.
   */
  @Data
  @ToString
  @EqualsAndHashCode
  @AllArgsConstructor
  private static class Image {
    
    private String imageUrl;
    
    private int height;
    
    private int width;
  }
  
  private String quizId;

  private String questionText;
  
  @JsonProperty("id")
  private String profileId;
  
  private Set<Image> images;
  
  /** Initialises the image property. */
  public UserProfileQuestion() {
    this.images = new LinkedHashSet<>();
  }
  
  public void addImage(String url, int height, int width) {
    this.images.add(new Image(url, height, width));
  }
}
