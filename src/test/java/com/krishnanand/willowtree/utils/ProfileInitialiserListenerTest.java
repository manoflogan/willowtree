// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.krishnanand.willowtree.service.IProfileService;

/**
 * @author krishnanand (Kartik Krishnanand)
 */
@RunWith(JUnit4.class)
public class ProfileInitialiserListenerTest {

  @Mock
  private IProfileService profileService;

  @Rule
  public MockitoRule rule = MockitoJUnit.rule();
  
  private ProfileInitialiserListener listener;
  
  @Before
  public void setUp() throws Exception {
    this.listener = new ProfileInitialiserListener(profileService);
  }

  @Test
  public void initialiseProfile() throws Exception {
    Mockito.when(this.profileService.initialiseProfiles()).thenReturn(true);
    this.listener.appReady(null);
    Mockito.verify(this.profileService).initialiseProfiles();
  }
}
