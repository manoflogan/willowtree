// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.model.Score;

/**
 * Initialises and creates a quiz object.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
public class QuizRepositoryImpl implements QuizRepositoryCustom {
  
  private final JpaTransactionManager jpaTransactionManager;
  
  private TransactionTemplate transactionTemplate;
  
  @PersistenceContext
  private EntityManager em;
  
  @Autowired
  public QuizRepositoryImpl(JpaTransactionManager jpaTransactionManager) {
    this.jpaTransactionManager = jpaTransactionManager;
    this.transactionTemplate = new TransactionTemplate(this.jpaTransactionManager);
  }

  /**
   * Initialises and returns a quiz attempt within a transaction.
   */
  @Override
  @Retryable(maxAttempts=5, value= {DataAccessException.class}, backoff=@Backoff(delay=2000))
  public Quiz registerQuiz() {
    String uniqueKey = UUID.randomUUID().toString().replaceAll("-", "");
    Quiz quiz = new Quiz();
    quiz.setQuizId(uniqueKey);
    Score score = new Score();
    quiz.setScore(score);
    score.setQuiz(quiz);
    this.transactionTemplate.execute(new TransactionCallback<Boolean>() {

      @Override
      public Boolean doInTransaction(TransactionStatus status) {
        em.persist(quiz);
        return true;
      }
      
    });
    
    return quiz;
  }

}
