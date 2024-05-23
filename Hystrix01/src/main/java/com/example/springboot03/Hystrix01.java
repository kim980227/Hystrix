package com.example.springboot03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCircuitBreaker //hystrix를 활성화 시킨다.
public class Hystrix01 {

  @Bean
  RestTemplate restTemplate(){
    return new RestTemplate();
  }
  public static void main(String[] args) {
    SpringApplication.run(Hystrix01.class, args);
  }

}
