package net.vincedgy.reservplat.reservationclient;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;


@EnableBinding(ReservationClientChannels.class)
@EnableCircuitBreaker
@RefreshScope
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class ReservationClientApplication {

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(ReservationClientApplication.class, args);
    }


    public Health health() {
        return Health.status("I <3  SpringBoot !!").build();
    }

}



interface ReservationClientChannels {
    @Output
    MessageChannel output();
}

class Reservation {
    private String reservationName;

    public String getReservationName() {
        return reservationName;
    }
}

@RestController
@RequestMapping("/reservations")
class ReservationApiGatewayRestController {

    private final RestTemplate restTemplate;

    @Autowired
    public ReservationApiGatewayRestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // fallback method for circuit breaker
    public Collection<String> fallback() {
        return new ArrayList<>();
    }

    @HystrixCommand(fallbackMethod = "fallback")
    @RequestMapping(method = RequestMethod.GET, value = "/names")
    Collection<String> names() {
        return this.restTemplate.exchange("http://reservation-service/reservations",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Resources<Reservation>>() {
                })
                .getBody()
                .getContent()
                .stream()
                .map(Reservation::getReservationName)
                .collect(Collectors.toList())
                ;
    }


    @Autowired
    private ReservationClientChannels outputChannelSource;

    @RequestMapping(method = RequestMethod.POST)
    public void write(@RequestBody Reservation reservation) {
        MessageChannel messageChannel = this.outputChannelSource.output();
        messageChannel.send(MessageBuilder.withPayload(reservation.getReservationName()).build());
    }


}





