### 요구 사항
#### 1. 선착순 100명 에게만 지급 되어야 한다.
#### 2. 101 개 이상이 지급되면 안된다.
#### 3. 순간적으로 몰리는 트래픽을 버틸 수 있어야 한다.

***
### DB 생성
#### 1. 

***
### Kafka 실행
#### 분산 이벤트 스트리밍 플랫폼
#### 이벤트 스트리밍이란 소스에서 목적지 까지 이벤트를 실시간으로 스트리밍 하는 것

### Docker Kafka Topic 리스트 확인
#### docker exec -it kafka kafka-topics --bootstrap-server localhost:9092 --list

### Docker Kafka topic coupon_create 생성 명령어
#### docker exec -it kafka kafka-console-producer.sh --topic coupon_create --broker-list 0.0.0.0:9092

### Docker Consumer 실행
#### docker exec -it kafka kafka-console-consumer.sh --topic coupon_create --bootstrap-server localhost:9092
***
### Redis 실행
#### 1. Docker Redis Image 사용
#### 2. Docker Redis 실행 명령어
#### docker run --name my-redis -p 6379:6379 redis
#### Docker Redis 접속
#### docker exec -it {{ container-name }} redis-cli
#### flushall 가지고 있는 값을 모두 날림
#### keys * 현재 Redis 에 있는 모든 key 값 들을 조회
#### incr {{ key }} ==> 해당 key 값에 count 를 증가 시켜 줌

### Docker Medis 실행하기 위하여서는
#### 가장 최근 버전의 Image
#### $docker image pull redis
#### # 버전 지정 pull - 예시
#### $docker image pull redis:6.0.18
#### 2-2. Docker 내부에 network 생성 및 확인
#### # network 생성
#### $docker network create redis-network
#### # network 확인.
#### $docker network ls

#### Redis Docker Run 실행 명령어
#### docker run --name my-redis \
#### -p 6379:6379 \
#### --network redis-network \
#### -d redis:latest redis-server \
#### --appendonly yes

### Redis 와 Kafka 를 사용하면 데이터 베이스의 부하를 줄여줄 수 있음 !
### 단, 처리 속도는 단지 실시간 성은 아님 Consumer 에서 처리를 하기 위하여서는 시간이 소모 됨 !