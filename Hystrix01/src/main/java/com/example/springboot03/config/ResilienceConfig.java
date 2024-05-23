package com.example.springboot03.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.time.Duration;

@Configuration
public class ResilienceConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 실패 비율 임계치 (50% 이상 실패하면 Circuit Breaker 작동)
                .waitDurationInOpenState(Duration.ofMillis(1000)) // 오픈 상태로 전환 후 대기 시간
                .ringBufferSizeInHalfOpenState(2) // 하프 오픈 상태에서 동시 실행을 허용하는 요청 수
                .ringBufferSizeInClosedState(2) // 클로즈 상태에서 요청 수
                .build();

        return CircuitBreakerRegistry.of(config);
    }
}
