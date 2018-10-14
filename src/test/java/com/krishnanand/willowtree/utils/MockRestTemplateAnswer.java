// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Mock implementation that stubs http requests to return a contents read from a local file.
 *  
 * @author krishnanand (Kartik Krishnanand)
 */
public abstract class MockRestTemplateAnswer<T> implements Answer<T> {
  
  private Map<String, String> urlToFileMap = new LinkedHashMap<>();
  
  public void addUrlToFileMapping(String url, String filePath) {
    this.urlToFileMap.put(url, filePath);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T answer(InvocationOnMock invocation) throws Throwable {
    String url = (String) invocation.getArguments()[0];
    if (!urlToFileMap.containsKey(url)) {
      return (T) invocation.callRealMethod();
    }
    String filePath = urlToFileMap.get(url);
    try(BufferedInputStream buf =
        new BufferedInputStream(ClassLoader.getSystemResourceAsStream(filePath));) {
      return parseInputStream(buf);
    }
  }
  
  /**
   * An implementation of this class will implement the method to parse the input stream.
   * @param is
   * @return
   */
  protected abstract T parseInputStream(InputStream is) throws Exception;

}
