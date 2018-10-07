// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.service;

/**
 * An instance of this class encapsulates all the functions related to profiles.
 * @author krishnanand (Kartik Krishnanand)
 */
public interface IProfileService {
  

  /**
   * Fetches the profiles to persist them in the repository.
   */
  boolean initialiseProfiles();

}
