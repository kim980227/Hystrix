package com.example.springboot03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Hystrix02 {
  @Bean
  RestTemplate restTemplate(){
    return new RestTemplate();
  }
  public static void main(String[] args) {
    SpringApplication.run(Hystrix02.class, args);
  }

}
