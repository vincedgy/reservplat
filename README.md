# reservplat

https://github.com/vincedgy/reservplat.git

My exploratory microservices plateform, following great presentation from the excellent Josh Long (https://github.com/joshlong)

# Microservices with SpringBoot, SpringCloud etc..

The goal : 
Build a reservation platform using microservices : Sprinboot REST API, JPA, Web (MVC), H2 db, HATEAOS

But also a whole bunch of great technologies from SpringCloud and Netflix microservices stack (quite standards now)
- Centralized Configuration server (wich is a microservice itself) Spring Config (https://cloud.spring.io/spring-cloud-config/) wich is part of Spring Cloud (http://projects.spring.io/spring-cloud/)
- Netflix Eureka integration for service discovery (https://github.com/Netflix/eureka)
- Twitter Zipkin   for distributed tracing (http://zipkin.io/)

## References 

- https://12factor.net/

- https://start.spring.io/

- https://github.com/spring-projects/spring-boot

- https://www.youtube.com/watch?v=rqQOSG0DWPY

- https://github.com/joshlong/bootiful-microservices-config

## SpringCloud Config server 

SpringCloud config uses a REAL git repository (wether it is local or remote).

For this project repo is https://github.com/vincedgy/reservplat-config.git

more here : https://cloud.spring.io/spring-cloud-config/spring-cloud-config.html

!! Look at Vault integration wich brings crypto capabilities for secret content

Don't forget to refresh client microservices on config by committing configuration into git and then POST empty request to refresh :

```
curl -d {} "http://localhost:8000/resfresh"
```

## Spring Boot CLI

https://github.com/spring-cloud/spring-cloud-cli

Unzip package of Spring CLI from here
http://repo.spring.io/release/org/springframework/boot/spring-boot-cli/1.5.3.RELEASE/spring-boot-cli-1.5.3.RELEASE-bin.zip

Then install 'cloud' library :
note : checkout the last release here https://github.com/spring-cloud/spring-cloud-cli/releases

```
$ spring install org.springframework.cloud:spring-cloud-cli:1.3.3.RELEASE
```

## Service registration and discovery with Eureka

When microservices need to talk to each other we don't want to let them know where other microservices are (what host, what port, etc...), and _DNS_ is a very bad choice in this case because of latency, cache etc...

Use @EnableDiscoveryClient in order to change from Eureka to another solution.

## Edge services

Responsible for talking to the external worl (from the microservices platform perspective).
It's edge services that talk to final clients of the services (mobile, car, refridgerators, TV, what you want...).

## Admin with Spring Boot Admin

http://codecentric.github.io/spring-boot-admin/1.5.0/

