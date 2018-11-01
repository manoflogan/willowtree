// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;

import javax.persistence.Column;
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
  @Column(name="social_link_id", nullable=false)
  private Long id;
  
  @Column(name="social_link_type")
  private String type;
  
  @Column(name="call_to_action")
  private String callToAction;
  
  @Column
  private String url;
  
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="profile_id")
  @JsonIgnore
  private UserProfile profile;
}
