// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An instance of this class encapsulates all the social links.
 *
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@EqualsAndHashCode(exclude= {"profile"})
@ToString(exclude= {"profile"})
@Entity
@Table(name="social_links")
public class SocialLinks implements Serializable {
  
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  @JsonIgnore
  @Getter
  @Setter
  private long id;
  
  @Setter
  @Getter
  private String type;
  
  @Setter
  @Getter
  private String callToAction;
  
  @Setter
  @Getter
  private String url;
  
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="profile_id")
  @Getter
  @Setter
  private UserProfile profile;

}
