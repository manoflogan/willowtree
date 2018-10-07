// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.service.IProfileService;

/**
 * Instance of this class encapsulates all the profiles of a controller.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@RestController
public class ProfileController {
  
  private final IProfileService profileService;
  
  @Autowired
  public ProfileController(IProfileService profileService) {
    this.profileService = profileService;
  }
  
  @PostMapping(name="/register", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  Quiz startQuiz() {
    return this.profileService.registerQuiz();
  }
}
