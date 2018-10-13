// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.krishnanand.willowtree.utils.QuestionType;

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
@Table(name="quiz_questions",
    uniqueConstraints={
    @UniqueConstraint(columnNames = {"id", "quiz_id"})})
public class QuizQuestion implements Serializable {
  
  @Id
  @GeneratedValue
  @Column(name="id")
  @JsonIgnore
  private Long id;

  /** Enumeration representing the question asked.*/
  @Enumerated(EnumType.STRING)
  private QuestionType questionType;

  /** Flag to indicate if the question has been answered correctly. */
  @Column(name="answered_correctly")
  private Boolean answeredCorrectly;
  
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="quiz_id")
  @JsonIgnore
  @JsonManagedReference
  private Quiz quiz;

  @OneToOne(cascade= {CascadeType.ALL}, mappedBy="quizQuestion", fetch=FetchType.EAGER)
  @JsonBackReference
  private QuizAnswer quizAnswer;

  @Column(name="total_attempts")
  private int totalAttempts;

  @Column(name="incorrect_attempts")
  private int incorrectAttempts;

  /**
   * Sets the default value to false.
   */
  @PrePersist
  @PreUpdate
  void markQuestionToFalseIfNotInitialised() {
    // Inserting false instead of nulls.
    if (this.answeredCorrectly == null) {
      answeredCorrectly = Boolean.FALSE;
    }
  }
}
