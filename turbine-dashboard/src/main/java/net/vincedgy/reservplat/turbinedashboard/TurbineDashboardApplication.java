package net.vincedgy.reservplat.turbinedashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTurbine
public class TurbineDashboardApplication {

	public static void main(String[] args) {

		SpringApplication.run(TurbineDashboardApplication.class, args);

		/*
		boolean cloudEnvironment = new StandardEnvironment().acceptsProfiles("cloud");
		new SpringApplicationBuilder(TurbineDashboardApplication.class).web(!cloudEnvironment).run(args);
		*/

	}
}
