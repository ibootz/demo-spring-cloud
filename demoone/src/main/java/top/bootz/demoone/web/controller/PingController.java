package top.bootz.demoone.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import top.bootz.common.constants.HttpConstants;
import top.bootz.demoone.dto.Pong;

@RestController
public class PingController {

	@GetMapping(value = "/ping")
	public Pong ping(HttpServletRequest request) {
		String localAddr = request.getLocalAddr() + ":" + request.getLocalPort();
		return new Pong(HttpConstants.Ack.OK.getCode(), HttpConstants.Ack.OK.getDesc(), localAddr);
	}

}
