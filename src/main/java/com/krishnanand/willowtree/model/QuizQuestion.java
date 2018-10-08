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
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name="quiz_questions", uniqueConstraints = {
    @UniqueConstraint(columnNames= {"quiz_id", "question_type"})
})
public class QuizQuestion implements Serializable {
  
  @Id
  @GeneratedValue
  @JsonIgnore
  @Column(name="id")
  private Long id;
  
  @Column(name="question_type")
  private String questionType;
  
  @ManyToOne(fetch=FetchType.EAGER)
  @JoinColumn(name="quiz_id")
  @JsonIgnore
  private Quiz quiz;
}
