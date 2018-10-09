// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.krishnanand.willowtree.model.Quiz;
import com.krishnanand.willowtree.model.Score;
import com.krishnanand.willowtree.model.UserProfile;

/**
 * Data access implementation to fetch questions 
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
   * Initialises and returns a quiz object representing a quiz game within a transaction language.
   */
  @Override
  @Retryable(maxAttempts=5, value= {Exception.class}, backoff=@Backoff(delay=2000))
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

  @Override
  @Retryable(maxAttempts=5, value= {Exception.class}, backoff=@Backoff(delay=2000))
  public List<UserProfile> fetchImagesQuestion(int count) {
    TypedQuery<Object[]> query =
        this.em.createQuery(
            "SELECT MIN(u.id), MAX(u.id) FROM UserProfile u WHERE u.headshot IS NOT NULL",
            Object[].class);
    Object[] numberOfRows = query.getSingleResult();
    long minId = (long) numberOfRows[0];
    long maxId = (long) numberOfRows[1];
    List<Long> profileIds = new ArrayList<>();
    int num = 0;
    SecureRandom rnd = new SecureRandom();
    while (num < 15) { // Arbitrarily large number
      int n = rnd.nextInt((int) maxId);
      if (n < minId) {
        continue;
      }
      profileIds.add((long) n);
      num ++; 
    }
    TypedQuery<UserProfile> userProfileQuery =
          this.em.createQuery(
              "SELECT u FROM UserProfile u WHERE u.headshot IS NOT NULL AND u.id IN:profileIds",
              UserProfile.class).setMaxResults(count);
    userProfileQuery.setParameter("profileIds", profileIds);
    List<UserProfile> userProfiles = userProfileQuery.getResultList();
    return userProfiles;
  }

}
