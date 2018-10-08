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
   * Initialises and returns a quiz attempt within a transaction.
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
  public List<UserProfile> fetchImagesQuestion(int count) {
    TypedQuery<Long> query =
        this.em.createQuery("SELECT COUNT(*) as cnt FROM UserProfile", Long.class);
    int numberOfRows = (int) (query.getSingleResult() != null ? query.getSingleResult().intValue() : 0);
    if (numberOfRows == 0) {
      return null;
    }
    SecureRandom random = new SecureRandom();
    int index = 0;
    List<UserProfile> userProfiles = new ArrayList<>();
    while (index < count) {
      int num = random.nextInt(count);
      TypedQuery<UserProfile> userProfileQuery =
          this.em.createQuery("SELECT u FROM UserProfile u",
              UserProfile.class).setFirstResult(num).setMaxResults(1);
      UserProfile userProfile = userProfileQuery.getSingleResult();
      if (userProfile.getHeadshot() == null) {
        continue;
      }
      userProfiles.add(userProfile);
      index ++;
    }
    return userProfiles;
  }

}
