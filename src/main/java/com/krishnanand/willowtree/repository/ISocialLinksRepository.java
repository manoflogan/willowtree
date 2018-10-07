// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.krishnanand.willowtree.model.SocialLinks;

/**
 * Repository to handle access operations for {@link SocialLinks}.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@Repository
public interface ISocialLinksRepository extends JpaRepository<SocialLinks, Long> {

}
