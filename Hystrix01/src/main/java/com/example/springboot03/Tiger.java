package com.example.springboot03;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.*;

@RestController
@RequestMapping("/app1")
class AppController {

    @Autowired
    RestTemplate rt;

    @GetMapping("/{num}") // sleep하는 인자를 전달
    @HystrixCommand(fallbackMethod = "tiger",
            commandProperties = {
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "1000"),    // 기본 타임아웃 설정( 응답을 기다리는 시간 = 현재 1초)

                    @HystrixProperty(
                            name = "circuitBreaker.errorThresholdPercentage",
                            value = "50"),     // 50퍼센트이상 문제가 발생하면 서킷을 오픈하시오.

                    @HystrixProperty(
                            name = "metrics.rollingStats.timeInMilliseconds",
                            value = "10000"), // 최근 10초 동안 3회이상 호출되면 통계 시작

                    @HystrixProperty(
                            name = "circuitBreaker.requestVolumeThreshold",
                            value = "3"),    // 최근 10초 동안 3회이상 호출되면 통계 시작

                    @HystrixProperty(
                            name = "circuitBreaker.sleepWindowInMilliseconds",
                            value = "8000") } )   // 서킷 오픈 유지 시간 -> 서킷이 오픈되면 서비스가 작동하지않는다(함수가 실행x)
            String f1(@PathVariable Integer num) {
        System.out.println("app1");
//    RestTemplate rt = new RestTemplate();

        // 현재 서비스 가능한 광고 1개를 주세요.
        String result = rt.getForObject("http://localhost:8082/bpp1/" + num, String.class);

        return "app1" + result;
    }

    public String tiger(Integer num, Throwable t) {
        System.out.println(t);
        return "기본 광고를 시작합니다." + num;
    }
}