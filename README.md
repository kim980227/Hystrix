# Hystrix
분산 시스템에서 장애에 대비하여 회로 차단 기능을 제공하는 오픈 소스 라이브러리

---

애플리케이션이 **외부 시스템으로 인한 서비스의 지연과 장애에 대해 내성**을 갖게 해주는 라이브러리로 Netflix에서 만들었으며, **현재는 유지보수만 진행**하고 있다고 합니다.

Hystrix에서 지원하는 기능은 다음과 같습니다.

- 빠른 실패(외부 시스템 혹은 서비스의 지연에 대한 빠른 실패처리)
- 기본적으로 Thread Pool을 이용하여 포화 상태가 될 경우 앞으로의 요청은 실패 처리
- 호출 실패에 대한 fallback 메소드 제공
- 연쇄 장애 방지
- Thread 혹은 Semaphore 전략에 따른 circuit breaker 기능
- 실시간 모니터링

---

# 예제 실습

---

### 의존성 추가

```java
<dependency>
	<groupId>
org.springframework.cloud
</groupId>
	<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
	<version>2.2.10.RELEASE</version>
</dependency>
```

### Hystrix01 코드

```jsx
@RestController
@RequestMapping("/app1")
class AppController {

    @Autowired
    RestTemplate rt;

    @GetMapping("/{num}")
    @HystrixCommand(fallbackMethod = "tiger",
            commandProperties = {
                    @HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "500"),    // 기본 타임아웃 설정( 응답을 기다리는 시간 )

                    @HystrixProperty(
                            name = "circuitBreaker.errorThresholdPercentage",
                            value = "25"),     // 25퍼센트이상 문제가 발생하면 서킷을 오픈하시오.

                    @HystrixProperty(
                            name = "metrics.rollingStats.timeInMilliseconds",
                            value = "10000"), // 최근 10초 동안 3회이상 호출되면 통계 시작

                    @HystrixProperty(
                            name = "circuitBreaker.requestVolumeThreshold",
                            value = "3"),    // 최근 10초 동안 3회이상 호출되면 통계 시작

                    @HystrixProperty(
                            name = "circuitBreaker.sleepWindowInMilliseconds",
                            value = "8000") } )   // 서킷 오픈 유지 시간 )
    String f1(@PathVariable Integer num) {
        System.out.println("app1");
//    RestTemplate rt = new RestTemplate();

        // 현재 서비스 가능한 광고 1개를 주세요.
        String result = rt.getForObject("http://localhost:8082/bpp1/" + num, String.class);

        return "app1" + result;
    }

    public String tiger(Integer num) {
        return "기본 광고를 시작합니다." + num;
    }
}
```

1. **`fallbackMethod = "tiger"`**: 메소드 호출 중에 예외가 발생하거나, 서킷이 열린 상태일 때 대신 호출되는 fallback 메소드의 이름을 지정합니다. "tiger"라는 이름의 메소드가 대체로 호출됩니다.
2. **`commandProperties`**: Hystrix Command의 속성들을 설정하는 블록입니다. 다음의 설정들이 포함되어 있습니다.
    - **`execution.isolation.thread.timeoutInMilliseconds`**: 기본 타임아웃 설정으로, 메소드 응답을 기다리는 최대 시간을 1000ms(1초)로 지정합니다.
    - **`circuitBreaker.errorThresholdPercentage`**: 서킷 브레이커가 작동하도록 설정되는데, 이 설정은 오류 비율에 기반합니다. 오류 비율이 25% 이상이면 서킷 브레이커가 열립니다.
    - **`metrics.rollingStats.timeInMilliseconds`**: 통계를 수집하는 기간으로, 최근 10초 동안 호출 횟수를 기준으로 통계를 수집합니다.
    - **`circuitBreaker.requestVolumeThreshold`**: 서킷 브레이커가 동작하기 위한 최소 호출 횟수입니다. 최근 10초 동안 3회 이상 호출되어야 서킷 브레이커가 동작합니다.
    - **`circuitBreaker.sleepWindowInMilliseconds`**: 서킷이 열린 후, 이 설정된 시간 동안 서킷을 열린 상태로 유지합니다. 8000ms(8초) 동안 서킷을 열린 상태로 유지하고, 이후에는 다시 시도합니다.

이렇게 설정된 Hystrix Command는 호출된 메소드의 성능, 오류율, 호출 횟수 등을 모니터링하며, 설정된 조건에 따라 서킷 브레이커를 열고 닫습니다. 서킷 브레이커가 열리면 fallback 메소드가 호출되어 예상치 못한 오류로부터 시스템을 보호하게 됩니다.

### Hystrix02 코드

```java
@RestController
@RequestMapping("/bpp1")
class BppController {
    @GetMapping("/{num}")
    String f1(@PathVariable Integer num) {
        System.out.println("bpp1");
        try {
            Thread.sleep(num);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "서비스 광고를 시작합니다.";
    }
}
```

# Hystrix란? - 자세한 설명

---

Hystrix는 분산 시스템에서 장애에 대비하여 회로 차단 기능을 제공하는 오픈 소스 라이브러리입니다. 이를 이해하기 위해 간단한 비유를 들어 설명해드리겠습니다.

가정해봅시다. 당신은 인터넷에서 온라인으로 물건을 구매하는 웹사이트를 운영하고 있습니다. 많은 사용자들이 동시에 여러 상품을 구매하고 있습니다. 그리고 여러 공급업체들과 연결되어 해당 상품의 가격을 실시간으로 가져와야 합니다. 문제는 공급업체들이 가끔씩 느려지거나 장애가 발생한다는 것입니다.

이런 상황에서 Hystrix는 여러분의 웹사이트가 공급업체의 장애에 영향을 받지 않고 원활하게 동작하도록 도와줍니다.

Hystrix의 동작 과정을 설명하면 다음과 같습니다:

1. **명령 실행 시도**: 사용자가 상품 가격을 조회하는 요청을 보냅니다. 이때 Hystrix는 해당 요청을 처리하는데 필요한 코드를 하나의 "명령"으로 정의합니다.
2. **장애 발생 감지**: Hystrix는 이 명령이 실행되는 도중 공급업체로의 요청이 지연되거나 오류가 발생하는지 지켜봅니다.
3. **탄력적 회로 차단**: 공급업체로의 요청에 문제가 생기면 Hystrix는 이상 현상을 탐지하고, 이후 해당 공급업체로의 요청을 일시적으로 차단합니다. 이때 차단된 명령을 "탄력적으로 차단(circuit open)"된 상태라고 합니다.
4. **폴백(Fallback) 처리**: 만약 명령이 차단된 상태라면, Hystrix는 미리 정의된 예비 처리 방법인 "폴백"을 실행합니다. 이는 사용자에게 오류 메시지를 보여주거나, 기본값을 반환하는 등 대체 처리를 수행하는 것입니다. 이로 인해 사용자들이 좋지 않은 경험을 하지 않도록 도와줍니다.
5. **차단된 회로의 자동 회복**: 일정 시간이 지나면 Hystrix는 자동으로 차단된 회로를 "탄력적으로 닫힌(circuit closed)" 상태로 변경합니다. 이후에는 다시 해당 공급업체로의 요청을 허용합니다. 그러나 만약 문제가 지속되면 다시 차단 상태로 전환하여 회로가 열리는 것을 방지합니다.

Hystrix를 사용하면 여러분의 웹사이트는 외부 서비스의 장애로부터 견고하게 보호될 수 있으며, 사용자들에게 안정적인 경험을 제공할 수 있습니다. 이러한 방식으로 Hystrix는 분산 시스템의 신뢰성과 내구성을 높이는데 도움을 주는 강력한 도구입니다.

# 서킷 브레이커란?

---

서킷 브레이커(Circuit Breaker)는 Hystrix와 같은 분산 시스템에서 사용되는 소프트웨어 패턴 중 하나입니다. 서킷 브레이커는 장애가 발생한 서비스나 외부 리소스로의 요청을 자동적으로 차단하는 메커니즘을 제공하여 시스템 전체의 안정성과 내구성을 향상시키는 데 도움을 줍니다.

서킷 브레이커를 전기 회로와 비유하면 다음과 같이 설명할 수 있습니다. 전기 회로에는 과부하가 발생했을 때 전기를 차단하는 퓨즈가 있습니다. 이 퓨즈는 회로를 보호하여 너무 많은 전류가 흐르는 것을 방지합니다. 마찬가지로 서킷 브레이커는 분산 시스템에서 과도한 요청이나 오류로 인해 시스템이 과부하 상태에 빠지는 것을 방지합니다.

서킷 브레이커의 동작 원리는 대략적으로 다음과 같습니다:

1. **시도 시도 (Attempt)**: 서킷 브레이커를 사용하는 시스템은 외부 서비스에 대한 요청을 보낼 때마다 이를 감시합니다.
2. **오류 탐지 (Error Detection)**: 만약 외부 서비스로의 요청이 일정 비율 이상으로 실패하거나 응답이 너무 느리게 오는 등의 문제가 발생하면, 서킷 브레이커는 이를 탐지합니다.
3. **서킷 열기 (Open the Circuit)**: 서킷 브레이커는 오류가 발생한 서비스로의 요청을 더 이상 보내지 않고, 해당 서비스와의 통신을 일시적으로 차단합니다. 이 상태를 "서킷이 열렸다(circuit is open)"고 합니다.
4. **폴백 처리 (Fallback Handling)**: 서킷이 열려있는 상태에서는 미리 정의된 폴백(Fallback) 로직이 실행됩니다. 이는 임시로 대체 응답을 제공하거나, 오류 메시지를 보여주는 등의 방식으로 사용자에게 좋지 않은 영향을 최소화합니다.
5. **서킷 재시도 (Circuit Retry)**: 일정 시간이 지나면 서킷 브레이커는 자동으로 서킷을 "반품시킵니다(circuit is half-open)" 상태로 전환합니다. 이제 서비스로의 요청을 다시 시도하며, 이때 성공한다면 서킷을 닫고(normal) 정상적으로 서비스에 요청을 보낼 수 있게 됩니다. 그러나 실패한다면 다시 서킷이 열립니다.

이렇게 서킷 브레이커는 시스템이 외부 서비스의 장애나 지연에 민감하게 반응하는 것을 방지하여 시스템의 안정성을 높이는데 도움을 줍니다. 따라서 서킷 브레이커는 분산 시스템에서 신뢰성 있는 서비스 운영을 위해 매우 유용한 패턴 중 하나입니다.
