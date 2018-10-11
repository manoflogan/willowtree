// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.utils;

/**
 * Defines the types of questions asked in a game.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
public enum QuestionType {
  
  /**
   * Indicates that the user has to identify profile from a list of headshots.
   */
  PROFILE_FROM_HEAD_SHOTS,
  
  /**
   * Indicates that the user has to identify headshot from a list of profiles
   */
  HEADSHOT_FROM_PROFILES,
  
  /**
   * Indicates the user has to select the correct Matt from list of headshots with different Matts
   */
  MATT_MODE
}
