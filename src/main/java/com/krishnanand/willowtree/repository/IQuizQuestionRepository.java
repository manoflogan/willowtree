// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.krishnanand.willowtree.model.QuizQuestion;

/**
 * Proxy repository to handle CRUD operations of {@link QuizQuestion}.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Repository
public interface IQuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

  @Query("select distinct qq.questionType from QuizQuestion qq where qq.quiz.quizId=?1")
  List<String> findQuestionTypesByQuizId(String quizId);

}
