package com.example.springboot03;

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
@RequestMapping("/bpp1")
class AppController {

    @Autowired
    RestTemplate rt;

    @GetMapping("{num}")
    String f2(@PathVariable Integer num) {
        System.out.println("bpp1");
        try {
            Thread.sleep(num); // 앱 타임아웃 설정(슬립하는동안 타임아웃을 발생하도록)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "서비스 광고를 시작합니다.";
    }
}