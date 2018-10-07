// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krishnanand.willowtree.model.Quiz;

/**
 * JPA placeholder repository.
 * @author krishnanand (Kartik Krishnanand)
 */
public interface QuizRepository extends JpaRepository<Quiz, Long>, QuizRepositoryCustom {

}
