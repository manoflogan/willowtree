// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Template definition of error handling for a model.
 * 
 * <p>Its subclass will be responsible for adding error objects to the model being returned.
 * 
 * @author krishnanand (Kartik Krishnanand)
 */
@JsonInclude(Include.NON_EMPTY)
public abstract class IError {
 
  static final class Error {
    private final int code;
    
    private final String message;

    public Error(int code, String message) {
      this.code = code;
      this.message = message;
    }

    public int getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Error [code=");
      builder.append(code);
      builder.append(", message=");
      builder.append(message);
      builder.append("]");
      return builder.toString();
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + code;
      result = prime * result + ((message == null) ? 0 : message.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      Error other = (Error) obj;
      if (code != other.code) {
        return false;
      }
      if (message == null) {
        if (other.message != null) {
          return false;
        }
      } else if (!message.equals(other.message)) {
        return false;
      }
      return true;
    }
  }
  
  private List<Error> errors;
  
  protected IError() {
    this.errors = new ArrayList<>();
  }
  
  /**
   * Returns a list of errors.
   */
  List<Error> getErrors() {
    return this.errors;
  }
  
  /**
   * Adds error the list of errors.
   * 
   * @param code error code
   * @param message error message
   */
  void addError(int code,  String message) {
    this.errors.add(new Error(code, message));
  }
}
