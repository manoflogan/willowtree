// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
  
  private static final Log LOG = LogFactory.getLog(QuizRepositoryImpl.class);
  
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
    if (LOG.isInfoEnabled()) {
      LOG.info("Initialising the quiz instance");
    }
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
    if (LOG.isInfoEnabled()) {
      LOG.info("Quiz profile initialised with quiz id" + quiz.getQuizId());
    }
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
    Set<Long> profileIds = new HashSet<>();
    if (LOG.isInfoEnabled()) {
      LOG.info(String.format("Fetching %d rows between %d and %d", count, minId, maxId));
    }
    int num = 0;
    SecureRandom rnd = new SecureRandom();
    while (num < 15) { // Arbitrarily large number
      int n = rnd.nextInt((int) maxId);
      if (n < minId) {
        continue;
      }
      if (!profileIds.contains((long) n)) {
        profileIds.add((long) n);
      }
      num ++; 
    }
    TypedQuery<UserProfile> userProfileQuery =
          this.em.createQuery(
              "SELECT u FROM UserProfile u WHERE u.headshot IS NOT NULL AND u.id IN:profileIds",
              UserProfile.class).setMaxResults(count);
    userProfileQuery.setParameter("profileIds", profileIds);
    return userProfileQuery.getResultList();
  }

}
