// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.krishnanand.willowtree.model.ProfileFetchStatus;
import com.krishnanand.willowtree.model.UserProfile;
import com.krishnanand.willowtree.repository.IProfileFetchStatusRepository;
import com.krishnanand.willowtree.repository.IUserProfileRepository;

/**
 * A single point of entry to all the profile related services.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Service
@PropertySource(value = "classpath:willowtree.properties", ignoreResourceNotFound = false)
public class ProfileService implements IProfileService {

  @Autowired
  private RestTemplate restTemplate;

  @Value("${init.url}")
  private String url;

  private final IProfileFetchStatusRepository fetchStatusRepository;
  
  private final IUserProfileRepository userProfileRepository;

  @Autowired
  ProfileService(IProfileFetchStatusRepository fetchStatusRepository,
      IUserProfileRepository userProfileRepository) {
    this.fetchStatusRepository = fetchStatusRepository;
    this.userProfileRepository = userProfileRepository;
  }

  /**
   * Initiates a one time initialisation of user profiles to persist them in the database.
   * 
   * <p>Implementation uses a profile fetch status to verify if the profiles have been intialised.
   * If they have not been intialised, then a HTTP request is made to fetch, deserialise, and 
   * persist the data.
   * 
   * <p><em>IMPORTANT</em>: To ensure that the profiles are initialised only once, it is recommended
   * that the initialisation process be invoked in a module that is invoked only once, such as
   * application listeners.
   */
  @Override
  @Transactional
  public boolean initialiseProfiles() {
    ProfileFetchStatus profileFetchStatus = this.fetchStatusRepository.findByFetched(Boolean.TRUE);
    if (profileFetchStatus == null || profileFetchStatus.getFetched() == null
        || !profileFetchStatus.getFetched().booleanValue()) {
      List<UserProfile> userProfiles = this.restTemplate.execute(this.url, HttpMethod.GET, null,
          new ResponseExtractor<List<UserProfile>>() {

            @Override
            public List<UserProfile> extractData(ClientHttpResponse response) throws IOException {
              try (InputStream is = response.getBody();) {
                ObjectMapper objectMapper = new ObjectMapper();
                SimpleModule module = new SimpleModule();
                module.addDeserializer(List.class, new UserProfileDeserialiser());
                objectMapper.registerModule(module);
                return objectMapper.readValue(is, new TypeReference<List<UserProfile>>() {});
              }
            }

          });
      this.userProfileRepository.saveAll(userProfiles);
      ProfileFetchStatus status = new ProfileFetchStatus();
      status.setFetched(Boolean.TRUE);
      status.setCreationTimeStamp(LocalDateTime.now(ZoneOffset.UTC));
      status.setLastModifiedTimeStamp(LocalDateTime.now(ZoneOffset.UTC));
      this.fetchStatusRepository.save(status);
    }
    return true;
  }

}
