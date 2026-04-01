package com.example.ecommerce;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy
@EnableCaching
public class EcomercceApplication {

  public static void main(String[] args) {

    SpringApplication.run(EcomercceApplication.class, args);
  }

}
