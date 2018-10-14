// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.service;

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.krishnanand.willowtree.model.UserProfile;

/**
 * Unit test for {@link UserProfileDeserialiser}.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@RunWith(JUnit4.class)
public class UserProfileDeserialiserTest {
  
  @Test
  public void testDeserialiser() throws Exception {
    try(InputStream is = ClassLoader.getSystemResourceAsStream("profiles.json")) {
      ObjectMapper objectMapper = new ObjectMapper();
      SimpleModule module = new SimpleModule();
      module.addDeserializer(List.class, new UserProfileDeserialiser());
      objectMapper.registerModule(module);
      List<UserProfile> userProfiles = objectMapper.readValue(
          is, new TypeReference<List<UserProfile>>() {});
      Assert.assertEquals(5, userProfiles.size());
      for (UserProfile userProfile : userProfiles) {
        Assert.assertNotNull(userProfile.getHeadshot());
      }
    }
  }

}
