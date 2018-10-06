// Copyright 2018 ManOf Logan. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * An instance of this class encapsulates a profile of the user.
 */
@Data
@EqualsAndHashCode
@ToString
@Entity
@Table(name="user_profile")
@JsonInclude(Include.NON_NULL)
public class UserProfile implements Serializable {

    @Getter
    @Setter
    @Id
    private String id;
 
    @Getter
    @Setter
    private String type;
    
    @Getter
    @Setter
    private String slug;
    
    @Getter
    @Setter
    private String jobTitle;
    
    @Getter
    @Setter
    private String firstName;
    
    @Getter
    @Setter
    private String lastName;
    
    @OneToOne(cascade= {CascadeType.ALL}, mappedBy="profile")
    @Getter
    @Setter
    private HeadShot headshot;
    
    @OneToMany(mappedBy="profile", cascade= {CascadeType.ALL})
    private Set<SocialLinks> socialLinks;
}
