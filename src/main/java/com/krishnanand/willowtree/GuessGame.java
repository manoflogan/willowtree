// Copyright 2018. All Rights Reserved.
package com.krishnanand.willowtree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Entry point to the application.
 */
@EnableRetry
@SpringBootApplication
@EnableJpaRepositories(considerNestedRepositories = true)
public class GuessGame {
    public static void main(String[] args) throws Exception {
      SpringApplication.run(GuessGame.class, args);
    }
}
