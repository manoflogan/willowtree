// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krishnanand.willowtree.model.Score;

/**
 * A proxy of this interface is used to encapsulate all functions related to managing
 * {@link Score} object.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
public interface ScoreRepository extends JpaRepository<Score, Long>, ScoreRepositoryCustom {

}
