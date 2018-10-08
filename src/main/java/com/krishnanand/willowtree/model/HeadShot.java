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
  
  @Getter
  @Setter
  @Column(name="headshot_type")
  private String type;
  
  @Getter
  @Setter
  @Column(name="mime_type")
  private String mimeType;
  
  @Getter
  @Setter
  @Column(name="headshot_id")
  @JsonProperty("id")
  private String headshotId;
  
  @Getter
  @Setter
  @Column
  private String url;
  
  @Getter
  @Setter
  @Column
  private String alt;
  
  @Getter
  @Setter
  @Column(nullable=true)
  private Integer height;
  
  @Getter
  @Setter
  @Column(nullable=true)
  private Integer width;
}
