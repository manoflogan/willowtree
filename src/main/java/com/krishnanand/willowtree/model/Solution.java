// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@ToString
@EqualsAndHashCode()
@Getter
@Setter
public class Solution extends IError {
    
    private String quizId;
    
    private String question;
    
    private Boolean correctAnswer;
    
    private Boolean playerAnswer;

    public String getQuestion() {
      return question;
    }

    public void setQuestion(String description) {
      this.question = description;
    }

    public String getQuizId() {
      return quizId;
    }

    public void setQuizId(String quizId) {
      this.quizId = quizId;
    }
    
    public Boolean getCorrectAnswer() {
      return correctAnswer;
    }

    public void setCorrectAnswer(Boolean correctAnswer) {
      this.correctAnswer = correctAnswer;
    }

    public Boolean getPlayerAnswer() {
      return playerAnswer;
    }

    public void setPlayerAnswer(Boolean playerAnswer) {
      this.playerAnswer = playerAnswer;
    }
}
