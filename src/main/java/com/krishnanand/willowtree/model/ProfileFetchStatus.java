// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An instance of this class verifies if the profiles have been fetched or not.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@Entity
@ToString
@EqualsAndHashCode
@Table(name="profile_fetch_status")
public class ProfileFetchStatus {
  
  @Id
  @GeneratedValue
  @Column(name="fetch_id")
  private Long fetchId;
  
  @Getter
  @Setter
  @Column(name="fetch_status")
  private Boolean fetched;
  
  @Getter
  @Setter
  @Column(name="created_time")
  private LocalDateTime creationTimeStamp;
  
  @Getter
  @Setter
  @Column(name="last_modified_time")
  private LocalDateTime lastModifiedTimeStamp;

}
