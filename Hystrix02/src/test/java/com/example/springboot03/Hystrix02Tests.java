package com.example.springboot03;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;


class Multiple{  // 곱셈 계산기
  int factorA,factorB;
  int result;

  Multiple(int factorA, int factorB){
    this.factorA = factorA;
    this.factorB = factorB;
    result = factorA * factorB;
  }

  int getResult(){
    return result;
  }
  void show(){
    System.out.println(factorA);
    System.out.println(factorB);
    System.out.println(result);
  }
}
interface RandomGenService{
  public int getNum();
}

@Service
class RandomGenServiceImpl implements RandomGenService{

  @Override
  public int getNum() {
//    return 10;
    return new Random().nextInt(10);
  }
}

interface MultipleService {
  public Multiple getMultiple();
}

@Service
class MultipleServiceImpl implements  MultipleService{
  @Autowired
  RandomGenService randomGenService;
  @Override
  public Multiple getMultiple() {
    return new Multiple(randomGenService.getNum(),randomGenService.getNum());
  }
}

@SpringBootTest
class Hystrix02Tests {

  @Autowired
  MultipleService multipleService;

  @Test
  void test() {
    Multiple multiple = multipleService.getMultiple();
    multiple.show();
  }
}
