// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.krishnanand.willowtree.model.HeadShot;
import com.krishnanand.willowtree.model.SocialLinks;
import com.krishnanand.willowtree.model.UserProfile;

/**
 * Deserialiser implementation to convert JSON array of list of objects. The example payload is
 * given below
 * <pre class="code">
 {
    id: "<unique_id>",
    type: "<string>",
    slug: "<name-slug>",
    jobTitle: "<title>",
    firstName: "John",
    lastName: "Doe",
    headshot: {
        type: "image",
        mimeType: "image/png",
        id: "<headshot_id>",
        url: "<url>",
        alt: "<image alt>",
        height: <image height in pixels>,
        width: <image width in pixels>
    },
    bio: "<bio>",
    socialLinks: [{
        type: "linkedin",
        callToAction: "<cta>",
        url: "<url>"
    }]
}
 * </pre>
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
public class UserProfileDeserialiser extends JsonDeserializer<List<UserProfile>> {
  
  private static final Log LOGGER = LogFactory.getLog(UserProfileDeserialiser.class);
  
  @Override
  public List<UserProfile> deserialize(JsonParser parser, DeserializationContext context)
      throws IOException, JsonProcessingException {
    JsonNode node = parser.getCodec().readTree(parser);
    LOGGER.debug(String.format("Initialising parsing of the %s user profiles.", node.size()));
    Iterator<JsonNode> iterator = node.elements();
    List<UserProfile> userProfiles = new ArrayList<>();
    while (iterator.hasNext()) {
      JsonNode jsonNode = iterator.next();
      UserProfile userProfile = new UserProfile();
      userProfile.setProfileId(jsonNode.get("id").asText());
      userProfile.setType(stringOrNull(jsonNode, "type"));
      userProfile.setSlug(stringOrNull(jsonNode, "slug"));
      userProfile.setJobTitle(stringOrNull(jsonNode, "jobTitle"));
      userProfile.setFirstName(stringOrNull(jsonNode, "firstName"));
      userProfile.setLastName(stringOrNull(jsonNode, "lastName"));
      userProfile.setBio(stringOrNull(jsonNode, "bio"));

      HeadShot headshot = userProfile.getHeadshot();
      JsonNode headshotNode = jsonNode.get("headshot");
      headshot.setType(stringOrNull(headshotNode, "type"));
      headshot.setMimeType(stringOrNull(headshotNode, "mimeType"));
      headshot.setHeadshotId(stringOrNull(headshotNode, "id"));
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
    }
    LOGGER.debug("Deserialising the profiles complete.");
    return userProfiles;
  }
  
  private String stringOrNull(JsonNode node, String attr) {
    return node.get(attr) != null ? node.get(attr).asText() : null;
  }
  
  private Integer intOrNull(JsonNode node, String attr) {
    return node.get(attr) != null ? node.get(attr).asInt() : null;
  }

}
