// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Template definition of error handling for a model.
 * 
 * <p>Its subclass will be responsible for adding error objects to the model being returned.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@JsonInclude(Include.NON_EMPTY)
@EqualsAndHashCode
@ToString()
public abstract class IError {
 
  @Data
  @ToString
  @EqualsAndHashCode
  @Getter
  static final class Error {
    private final int code;
    
    private final String message;

    public Error(int code, String message) {
      this.code = code;
      this.message = message;
    }
  }
  
  private List<Error> errors;
  
  protected IError() {
    this.errors = new ArrayList<>();
  }
  
  /**
   * Returns a list of errors.
   */
  public List<Error> getErrors() {
    return this.errors;
  }
  
  /**
   * Adds error the list of errors.
   * 
   * @param code error code
   * @param message error message
   */
  public void addError(int code,  String message) {
    this.errors.add(new Error(code, message));
  }
}
