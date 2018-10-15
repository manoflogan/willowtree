// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
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
  
  @OneToOne(cascade= {CascadeType.ALL},mappedBy="quiz", fetch = FetchType.EAGER)
  @JsonBackReference(value="quiz-score")
  private Score score;
  
  @OneToMany(cascade= {CascadeType.ALL}, mappedBy="quiz", fetch = FetchType.EAGER)
  @JsonBackReference(value="quiz-questions")
  private Set<QuizQuestion> quizQuestions;

  @Column(name="created_timestamp")
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalDateTime creationTimestamp;

  @Column(name="last_updated_timestamp")
  @JsonIgnore
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  private LocalDateTime lastModifiedTimestamp;
  
  public Set<QuizQuestion> getQuizQuestions() {
    if (this.quizQuestions == null) {
      this.quizQuestions = new LinkedHashSet<>();
    }
    return this.quizQuestions;
  }

  @PrePersist
  void updateTimestamp() {
    LocalDateTime utcNow = LocalDateTime.now(ZoneOffset.UTC);
    if (creationTimestamp == null) {
      this.creationTimestamp = utcNow;
    }
    this.lastModifiedTimestamp = utcNow;
  }
}
