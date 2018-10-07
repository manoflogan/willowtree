// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An instance of this encapsulates the score for a given quiz.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Entity
@Table(name="quiz_score")
@Data
@ToString(exclude= {"quiz"})
@EqualsAndHashCode(exclude={"quiz"})
public class Score implements Serializable {
  
  @Id
  @GeneratedValue
  @JsonIgnore
  @Getter
  @Setter
  @Column(name="score_id")
  private Long id;
  
  @Getter
  @Setter
  @OneToOne
  @JoinColumn(nullable=false, name="id")
  @JsonManagedReference
  private Quiz quiz;
  
  @Getter
  @Setter
  @Column(name="correct_answers")
  private int correctAnswers;
  
  @Getter
  @Setter
  @Column(name="incorrect_answers")
  private int incorrectAnswers;
}
