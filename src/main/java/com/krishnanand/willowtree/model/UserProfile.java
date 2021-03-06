// Copyright 2018 ManOf Logan. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * An instance of this class encapsulates a profile of the user.
 */
@Data
@Entity
@Table(name = "user_profile", indexes= {
    @Index(columnList="first_name", name="index_profile_first_name"),
    @Index(columnList="last_name", name="index_profile_last_name"),
    @Index(columnList="profile_type", name="index_profile_type")
})
@JsonInclude(Include.NON_NULL)
public class UserProfile implements Serializable {
  
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @JsonIgnore
  private Long id;

  @Column(name = "profile_id", nullable = false)
  @JsonProperty("id")
  private String profileId;

  @Column(name = "profile_type")
  private String type;

  @Column(nullable = false)
  private String slug;

  @Column(name = "job_title")
  private String jobTitle;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name="bio")
  private String bio;

  @OneToOne(cascade = {CascadeType.ALL}, mappedBy = "profile", optional=false, fetch=FetchType.LAZY)
  @Setter(AccessLevel.NONE)
  private HeadShot headshot;

  @OneToMany(mappedBy = "profile", cascade = {CascadeType.ALL}, fetch=FetchType.LAZY)
  @Setter(AccessLevel.NONE)
  private Set<SocialLinks> socialLinks;

  public HeadShot getHeadshot() {
    if (this.headshot == null) {
      this.headshot = new HeadShot();
    }
    return this.headshot;
  }

  public Set<SocialLinks> getSocialLinks() {
    if (this.socialLinks == null) {
      this.socialLinks = new LinkedHashSet<>();
    }
    return this.socialLinks;
  }

  // Set default values.
  @PrePersist
  @PreUpdate
  public void initialiseNulls() {
    if (this.jobTitle == null) {
      this.jobTitle = "";
    }
  }
}
