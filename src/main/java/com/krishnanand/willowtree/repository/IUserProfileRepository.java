// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.krishnanand.willowtree.model.UserProfile;

/**
 * Repository to handle user profiles.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Repository
public interface IUserProfileRepository extends JpaRepository<UserProfile, Long> {

}
