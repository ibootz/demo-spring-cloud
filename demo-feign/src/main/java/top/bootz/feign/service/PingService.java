package top.bootz.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import top.bootz.feign.dto.Pong;

@FeignClient(name = "demoone", path = "/demo")
public interface PingService {

	@GetMapping(value = "/ping")
	public Pong ping();

}
