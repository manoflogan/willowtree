// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * An instance of this class encapsulates the question to be answered by the participant.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
@JsonInclude(Include.NON_EMPTY)
public class UserProfileQuestion extends IError {
  
  /**
   * An instance of this class is used to encapsulate the image metadata.
   */
  @Data
  @ToString
  @EqualsAndHashCode
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonInclude(Include.NON_EMPTY)
  private static class Image {
    
    private String imageUrl;
    
    private Integer height;
    
    private Integer width;

    @JsonProperty("id")
    private String headshotId;
  }
  
  private String quizId;
  
  private Long questionId;

  private String questionText;
  
  private Set<Image> images;
  
  /** Initialises the image property. */
  public UserProfileQuestion() {
    this.images = new LinkedHashSet<>();
  }

  public void addImage(String url, Integer height, Integer width, String headshotId) {
    this.images.add(new Image(url, height, width, headshotId));
  }
}
