# stock microservice
Stock/Product Microservice for Cloud Native Post Academy Deep Dive

개요
분산된 마이크로서비스에서 일관성을 유지하기 위해 잘 알려준 방법으로 크리스 리차드처럼
이벤트 기반으로 하는것과 REST Try-Confirm/Cancel 기반의 분산 트랜잭션을 처리하는 방법이 있다.

이 학습에서는 REST 기반 분산트랜잭션을 처리하여 일관성을 유지하는 방법으로 구현된 3 개의 마이크로서비스를
컨테이터화 작업하는 것을 학습합니다.

![alt text](img/rest_tcc.png)

## 필수작업
 1. iks 배포후 상품정보를 먼저 등록

![alt text](img/post_product.png)

 2. postgres db 접속 결과 확인

 3. kafka topic 조회
    kafka topic 정보를 조회합니다.
    kafka 정보
    * kafka name: kafka
    * zookeeper name: kafka-zookeeper

 4. redis 상품정보 조회
    상품주문 후 사용자가 등록한 상품정보가 캐시된 것을 확인합니다.
