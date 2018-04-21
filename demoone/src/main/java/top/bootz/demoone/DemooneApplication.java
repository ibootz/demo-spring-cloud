package top.bootz.demoone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableDiscoveryClient
@SpringBootApplication
public class DemooneApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(DemooneApplication.class, args);
	}

}
