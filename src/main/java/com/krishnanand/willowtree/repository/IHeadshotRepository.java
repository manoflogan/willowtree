// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.krishnanand.willowtree.model.HeadShot;

/**
 * Repository to handle access operations for {@link HeadShot}.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Repository
public interface IHeadshotRepository extends JpaRepository<HeadShot, Long> {

}
