// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An instance of this class encapsulates the registration information.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name="quiz", indexes= {
    @Index(columnList="quiz_id", name="index_quiz_id")
})
@JsonInclude(Include.NON_EMPTY)
public class Quiz implements Serializable {
  
  @Column(name="quiz_id", nullable=false, unique=true)
  private String quizId;
  
  @Column()
  @Id
  @GeneratedValue
  @JsonIgnore
  private Long id;
  
  @OneToOne(cascade= {CascadeType.ALL},mappedBy="quiz")
  @JsonBackReference
  private Score score;
  
  @OneToMany(cascade= {CascadeType.ALL}, mappedBy="quiz")
  @JsonBackReference
  private Set<QuizQuestion> quizQuestions;
  
  public Set<QuizQuestion> getQuizQuestions() {
    if (this.quizQuestions == null) {
      this.quizQuestions = new LinkedHashSet<>();
    }
    return this.quizQuestions;
  }
}
