// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.utils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.krishnanand.willowtree.service.IProfileService;

/**
 * Listener responsible for performing a one time initialisation.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Component
public class ProfileInitialiserListener implements ApplicationListener<ContextRefreshedEvent> {
  
  private final IProfileService profileService;
  
  @Autowired
  public ProfileInitialiserListener(IProfileService profileService) {
    this.profileService = profileService;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    this.profileService.initialiseProfiles();
  }
  
  @PostConstruct
  void setH2DbProperties() {
    System.setProperty("h2.implicitRelativePath", "true");
  }
  
  @PreDestroy
  void removeH2DbProperties() {
    System.clearProperty("h2.implicitRelativePath");
  }
}
