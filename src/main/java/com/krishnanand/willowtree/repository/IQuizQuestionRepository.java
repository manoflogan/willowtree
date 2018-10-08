// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krishnanand.willowtree.model.QuizQuestion;

/**
 * Proxy repository to handle CRUD operations of {@link QuizQuestion}.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
public interface IQuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {

}
