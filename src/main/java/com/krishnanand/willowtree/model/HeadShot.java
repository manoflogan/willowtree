// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@EqualsAndHashCode(exclude= {"profile"})
@ToString(exclude= {"profile"})
@Entity
@Table(name="head_shot", 
    indexes= {
        @Index(name="index_headshot_id", columnList="headshot_id"),
        @Index(name="index_headshot_url", columnList="url")
    })
public class HeadShot implements Serializable {
  
  @Id
  @GeneratedValue
  @JsonIgnore
  @Column(nullable=false)
  private Long id;
    
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  private UserProfile profile;
  
  @Column(name="headshot_type", nullable=true)
  private String type;
  
  @Column(name="mime_type", nullable=true)
  private String mimeType;
  
  @Column(name="headshot_id", nullable=true)
  @JsonProperty("id")
  private String headshotId;
  
  @Column(nullable=true)
  private String url;
  
  @Column(nullable=true)
  private String alt;
  
  @Column(nullable=true)
  private Integer height;
  
  @Column(nullable=true)
  private Integer width;
}
