// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An instance of this class encapsulates the question asked.
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@ToString(exclude="quiz")
@EqualsAndHashCode(exclude="quiz")
@Entity
@Getter
@Setter
@Table(name="quiz_questions")
public class QuizQuestion implements Serializable {
  
  @Id
  @GeneratedValue
  @Column(name="id")
  @JsonIgnore
  private Long id;
  
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="quiz_id")
  @JsonIgnore
  @JsonManagedReference
  private Quiz quiz;
}
