package net.vincedgy.reservplat.reservationservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

interface ReservationServiceChannels {
    @Input
    SubscribableChannel input();
}

@EnableBinding(ReservationServiceChannels.class)
@SpringBootApplication
@EnableDiscoveryClient
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}

}

@RestController
@RefreshScope
class MessageRestController {
    @Value("${message}")
    private String message;

    @RequestMapping("/message")
    String readMessage() {
        return this.message;
    }

}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAll();
    List<Reservation> findById(Long id);
}


@MessageEndpoint
class ReservationProcessor {
    private final ReservationRepository reservationRepository;

    @ServiceActivator(inputChannel = "input")
    public void acceptNewReservations(String rn) {
        reservationRepository.save(new Reservation(rn));
    }

    @Autowired
    public ReservationProcessor(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
}

@Component
class ReservationDataCLR implements CommandLineRunner {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationDataCLR(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Stream.of("Spencer", "Vincent", "Catherine", "Louis-Philippe", "Emmanuel", "François", "Jacques")
                .forEach( n -> reservationRepository.save(new Reservation(n)));

        reservationRepository.findAll().forEach(System.out::println);
    }
}

@Entity
class Reservation {
	public Reservation() {} // JPA

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String reservationName;

    public Reservation(String n) {
        this.reservationName=n;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", reservationName='" + reservationName + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservationName() {
        return reservationName;
    }

    public void setReservationName(String reservationName) {
        this.reservationName = reservationName;
    }
}
