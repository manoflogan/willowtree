// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.util;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.krishnanand.willowtree.model.UserProfile;
import com.krishnanand.willowtree.service.UserProfileDeserialiser;

/**
 * @author krishnanand (Kartik Krishnanand)
 */
public class UserProfileAnswer extends MockRestTemplateAnswer<List<UserProfile>> {

  /**
   * Parses the inputstream to return user profiles.
   */
  @Override
  protected List<UserProfile> parseInputStream(InputStream is) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(List.class, new UserProfileDeserialiser());
    objectMapper.registerModule(module);
    return objectMapper.readValue(
        is, new TypeReference<List<UserProfile>>() {});
  }

}
