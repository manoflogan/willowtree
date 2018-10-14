// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An instance of this class encapsulates the answer of the quiz.
 *
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@Getter
@Setter
@ToString(exclude= {"quizQuestion"})
@EqualsAndHashCode(exclude= {"quizQuestion"})
@Entity
@Table(name="quiz_answer")
public class QuizAnswer {
  
  @Id
  @GeneratedValue
  @Column(name="id")
  @JsonIgnore
  private Long id;

  @OneToOne(fetch=FetchType.EAGER)
  @MapsId
  @JsonManagedReference
  private QuizQuestion quizQuestion;
  
  @Column(name="correct_answer")
  private String correctAnswer;
}
