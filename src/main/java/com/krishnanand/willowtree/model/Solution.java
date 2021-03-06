// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * An instance of this value object encapsulates both the system and the user provided answer.
 *
 * @author krishnanand (Kartik Krishnanand)
 */
@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
public class Solution extends IError {
    
    private String quizId;
    
    private Long questionId;
    
    private Boolean isCorrect;
    
    private String playerAnswer;
}
