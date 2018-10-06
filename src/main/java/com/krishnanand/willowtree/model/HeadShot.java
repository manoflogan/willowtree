// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Instance of this class encapsulates the information about the headshot.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name="head_shot")
public class HeadShot implements Serializable {
  
  @Id
  @OneToOne()
  @JoinColumn(name="profile_id")
  private UserProfile profile;
  
  @Getter
  @Setter
  private String type;
  
  @Getter
  @Setter
  private String mimeType;
  
  @Getter
  @Setter
  private String id;
  
  @Getter
  @Setter
  private String url;
  
  @Getter
  @Setter
  private String alt;
  
  @Getter
  @Setter
  private int height;
  
  @Getter
  @Setter
  private int width;
}
