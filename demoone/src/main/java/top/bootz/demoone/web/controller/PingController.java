package top.bootz.demoone.web.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import top.bootz.common.HttpConstants;
import top.bootz.demoone.api.dto.Pong;
import top.bootz.demoone.api.service.PingRemoteService;

@RestController
public class PingController implements PingRemoteService {

	@Override
	public Pong ping(@RequestParam("localAddr") String localAddr) {
		return new Pong(HttpConstants.Ack.OK.getCode(), HttpConstants.Ack.OK.getDesc(), localAddr);
	}

}
