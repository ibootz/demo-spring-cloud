package top.bootz.feign.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import top.bootz.demoone.api.dto.Pong;
import top.bootz.feign.service.PingService;

@RestController
public class PingController {
	
	@Autowired
	private PingService pingService;

	@GetMapping(value = "/feign-ping", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public Pong pingConsumer(HttpServletRequest req) {
		String localAddr = req.getLocalAddr() + ":" + req.getLocalPort();
		return pingService.ping(localAddr);
	}

}
