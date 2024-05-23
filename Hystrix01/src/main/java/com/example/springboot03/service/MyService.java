package com.example.springboot03.service;

import org.springframework.stereotype.Service;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

@Service
public class MyService {

    private final CircuitBreaker circuitBreaker;

    public MyService(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("myCircuitBreaker");
    }

    public String someServiceCall() {
        // Resilience4j Circuit Breaker 적용
        return CircuitBreaker.decorateSupplier(circuitBreaker, this::callExternalService)
                .get();
    }

    private String callExternalService() {
        // 외부 서비스 호출 (예제에서는 간단하게 문자열 반환)
        // 실제 서비스는 외부 서비스를 호출하고, 예외 발생 등을 처리해야 합니다.
        return "External Service Response";
    }
}
