## Spring Data Redis Cache - Redis-CLI

This Spring Boot project is designed to integrate PostgreSQL as the database and Redis for efficient caching operations. The Redis CLI (Command Line Interface) is utilized for cache management and monitoring.

👉 [For more information, you can check out my blog post.](https://ayseozcan.com/2024/01/31/spring-data-redis-cache-redis-cli/)
### Requirements

- IDE
- JDK 17+
- Gradle 7.5+

### Setup PostgreSQL and Redis

- docker-compose.yaml

```yaml
version: '3.8'

services:
  db:
    image: postgres
    restart: always
    environment:
      - POSTGRES_PASSWORD=${POSTGRES_PASS}
      - POSTGRES_USER=postgres
      - POSTGRES_DB=RedisExample
    ports:
      - "5432:5432"
    networks:
      - redis

  redis:
    image: redis:latest
    environment:
      - REDIS_PASSWORD=secret
      - REDIS_DB=1 #default 0 - 16 databases, indexed 0–15.
    ports:
      - "6379:6379"
    networks:
      - redis

networks:
  redis:
    driver: bridge
```
```
docker-compose up
```
This command above reads the specified docker-compose.yaml file, creates and starts Docker containers based on the defined services and configurations.

### Execute Redis-CLI

```
docker exec -it <container_name> redis-cli
```
### Dependencies

|                       Implementation                        | Version |
|:-----------------------------------------------------------:|:-------:|
|     "org.springframework.boot:spring-boot-starter-web"      |  3.2.2  |
|  "org.springframework.boot:spring-boot-starter-data-redis"  |  3.2.2  |
|   "org.springframework.boot:spring-boot-starter-data-jpa"   |  3.2.2  |
|                "org.postgresql:postgresql "                 | 42.6.0  |

### application.yaml

```yaml
server:
  port: 9090

spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: ${POSTGRES_PASS}
    url: jdbc:postgresql://localhost:5432/RedisExample
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  cache:
    type: redis
    host: localhost
    password: secret
    port: 6379
```
### Redis Configuration

```java
@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.cache.host}")
    private String hostName;

    @Value("${spring.cache.port}")
    private int port;

    @Value("${spring.cache.password}")
    private String password;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostName,port);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
}
```
### Getting started

1. Clone this project and run.

```
https://github.com/ayse-ozcan/spring-data-redis-cache.git
```
2. Open Postman.

- Send a POST request to save: `http://localhost:9090/api/v1/users/save/:username`
- Send a GET request to find-user : `http://localhost:9090/api/v1/users/find-by-username/:username`
- Review all the keys via Redis-CLI.

```shell
127.0.0.1:6379> KEYS *
1) "user::mishakp"
2) "user::rbedburys"
3) "user::brizonf"
```
- Get specific key.
```shell
127.0.0.1:6379> GET user::brizonf
```
- Send a GET request to clear-cache : `http://localhost:9090/api/v1/users/clear-cache`

```shell
127.0.0.1:6379> KEYS *
(empty array)
```
- Also, clear all cache with the `FLUSHALL` command.

```shell
127.0.0.1:6379> FLUSHALL
OK
```
