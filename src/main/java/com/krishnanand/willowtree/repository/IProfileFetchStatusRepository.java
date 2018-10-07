// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.krishnanand.willowtree.model.ProfileFetchStatus;

/**
 *  Repository to managing the profile fetch status.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Repository
public interface IProfileFetchStatusRepository extends JpaRepository<ProfileFetchStatus, Long>{
  
  /**
   * Returns ProfileFetchStatus by {@code fetchStatus}.
   * 
   * @return {@code null} if not found; value object if otherwise
   */
  ProfileFetchStatus findByFetched(boolean fetchStatus);

}
