// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.utils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.krishnanand.willowtree.service.IProfileService;

/**
 * Listener responsible for performing a one time initialisation.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Component
public class ProfileInitialiserListener {
  
  private final IProfileService profileService;
  
  @Autowired
  public ProfileInitialiserListener(IProfileService profileService) {
    this.profileService = profileService;
  }

  @EventListener
  public void appReady(ApplicationReadyEvent event) {
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
