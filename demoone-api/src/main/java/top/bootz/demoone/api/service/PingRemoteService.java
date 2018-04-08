package top.bootz.demoone.api.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import top.bootz.demoone.api.dto.Pong;

public interface PingRemoteService {

	@GetMapping(value = "/ping")
	public Pong ping(@RequestParam("localAddr") String localAddr);

}
