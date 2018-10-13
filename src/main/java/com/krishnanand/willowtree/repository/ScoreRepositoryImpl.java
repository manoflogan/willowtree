// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.krishnanand.willowtree.model.QuizQuestion;
import com.krishnanand.willowtree.model.Score;
import com.krishnanand.willowtree.model.Solution;
import com.krishnanand.willowtree.model.UserAnswer;

/**
 * @author krishnanand (Kartik Krishnanand)
 */
public class ScoreRepositoryImpl implements ScoreRepositoryCustom {

  private final JpaTransactionManager jpaTransactionManager;
  
  private TransactionTemplate transactionTemplate;

  @PersistenceContext
  private EntityManager em;

  @Autowired
  public ScoreRepositoryImpl(JpaTransactionManager jpaTransactionManager) {
    this.jpaTransactionManager = jpaTransactionManager;
    this.transactionTemplate = new TransactionTemplate(this.jpaTransactionManager);
  }

  /**
   * The strategy implementation carries for the following actions.
   * <ul>
   *   <li>Compares the user provided answer with the sytem answer.</li>
   *   <li>If the answer is correct, then
   *     <ul>
   *       <li>Increments the score's correct answer by 1, and persists to the database.</li>
   *       <li>Marks the question as answered.This means that the system will not accept any more
   *       answers against the question.</li>
   *     </ul>
   *   </li>
   *   <li>Increments the number of attempts of the question.</li>
   * </ul>
   */
  @Override
  public Solution updateScore(QuizQuestion quizQuestion, UserAnswer answer) {
    return this.transactionTemplate.execute(new TransactionCallback<Solution> () {

      @Override
      public Solution doInTransaction(TransactionStatus status) {
        Score score = quizQuestion.getQuiz().getScore();
        Solution solution = new Solution();
        solution.setQuestionId(answer.getQuestionId());
        solution.setQuizId(answer.getQuizId());
        solution.setPlayerAnswer(answer.getAnswer());
        if (quizQuestion.getQuizAnswer().getCorrectAnswer().equals(answer.getAnswer())) {
          solution.setIsCorrect(Boolean.TRUE);
          quizQuestion.setAnsweredCorrectly(true);
          score.setScore(score.getScore() + 1);
          em.merge(score);
        } else {
          solution.setIsCorrect(Boolean.FALSE);
          quizQuestion.setIncorrectAttempts(quizQuestion.getIncorrectAttempts() + 1);
        }
        quizQuestion.setTotalAttempts(quizQuestion.getTotalAttempts() + 1);
        return solution;
      }
      
    });
    
  }

}
