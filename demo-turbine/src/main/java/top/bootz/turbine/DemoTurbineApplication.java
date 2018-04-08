package top.bootz.turbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication
@EnableTurbine
@EnableDiscoveryClient
public class DemoTurbineApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoTurbineApplication.class, args);
	}
}
