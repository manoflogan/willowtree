// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.krishnanand.willowtree.model.HeadShot;
import com.krishnanand.willowtree.model.SocialLinks;
import com.krishnanand.willowtree.model.UserProfile;

/**
 * @author krishnanand (Kartik Krishnanand)
 */
@Component
@PropertySource(value="classpath:willowtree.properties", ignoreResourceNotFound=false)
public class ProfileInitialiser implements ApplicationListener<ContextRefreshedEvent> {
  
  private static final Log LOGGER = LogFactory.getLog(ProfileInitialiser.class);
  
  @Value("${init.url}")
  private String url;
  
  private RestTemplate restTemplate;
  
  @Autowired
  public ProfileInitialiser(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  
  private static class UserProfileSerializer extends JsonDeserializer<List<UserProfile>> {

    @Override
    public List<UserProfile> deserialize(JsonParser parser, DeserializationContext context)
        throws IOException, JsonProcessingException {
      JsonNode node = parser.getCodec().readTree(parser);
      LOGGER.debug("Initialising parsing");
      Iterator<JsonNode> iterator = node.elements();
      List<UserProfile> userProfiles = new ArrayList<>();
      int count = 0;
      while (iterator.hasNext()) {
        JsonNode jsonNode = iterator.next();
        UserProfile userProfile = new UserProfile();
        userProfile.setId(jsonNode.get("id").asText());
        userProfile.setType(stringOrNull(jsonNode, "type"));
        userProfile.setSlug(stringOrNull(jsonNode, "slug"));
        userProfile.setJobTitle(stringOrNull(jsonNode, "jobTitle"));
        userProfile.setFirstName(stringOrNull(jsonNode, "firstName"));
        userProfile.setLastName(stringOrNull(jsonNode, "lastName"));

        HeadShot headshot = userProfile.getHeadshot();
        JsonNode headshotNode = jsonNode.get("headshot");
        headshot.setType(stringOrNull(headshotNode, "type"));
        headshot.setMimeType(stringOrNull(headshotNode, "mimeType"));
        headshot.setId(stringOrNull(headshotNode, "id"));
        headshot.setUrl(stringOrNull(headshotNode, "url"));
        headshot.setAlt(stringOrNull(headshotNode, "alt"));
        headshot.setHeight(intOrNull(headshotNode, "height"));
        headshot.setWidth(intOrNull(headshotNode, "width"));
        headshot.setProfile(userProfile);
        
        JsonNode socialLinksNode = jsonNode.get("socialLinks");
        Iterator<JsonNode> socialLinksIter = socialLinksNode.iterator();
        Set<SocialLinks> socialLinks = userProfile.getSocialLinks();
        while (socialLinksIter.hasNext()) {
          SocialLinks socialLink = new SocialLinks();
          JsonNode linkNode = socialLinksIter.next();
          socialLink.setType(stringOrNull(linkNode, "type"));
          socialLink.setCallToAction(stringOrNull(linkNode, "callToAction"));
          socialLink.setUrl(stringOrNull(linkNode, "url"));
          socialLink.setProfile(userProfile);
          socialLinks.add(socialLink);
        }
        userProfiles.add(userProfile);
        LOGGER.debug("Object ID : " + count ++);
      }
      return userProfiles;
    }
    
    private String stringOrNull(JsonNode node, String attr) {
      return node.get(attr) != null ? node.get(attr).asText() : null;
    }
    
    private Integer intOrNull(JsonNode node, String attr) {
      return node.get(attr) != null ? node.get(attr).asInt() : null;
    }
    
  }
  
  

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    List<UserProfile> userProfiles =
         this.restTemplate.execute(this.url, HttpMethod.GET, null, new ResponseExtractor<List<UserProfile>>() {

          @Override
          public List<UserProfile> extractData(ClientHttpResponse response) throws IOException {
            try(InputStream is =  response.getBody();) {
              ObjectMapper objectMapper = new ObjectMapper();
              SimpleModule module = new SimpleModule();
              module.addDeserializer(List.class, new UserProfileSerializer());
              objectMapper.registerModule(module);
              
              return objectMapper.readValue(is, new TypeReference<List<UserProfile>>() {});
            }
          }
           
         });
    
    System.out.println(userProfiles);
  }
}
