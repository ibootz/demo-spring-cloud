package top.bootz.feign.service;

import top.bootz.common.HttpConstants;
import top.bootz.demoone.api.dto.Order4Get;
import top.bootz.demoone.api.dto.Pong;

public class DefaultFallback implements OrderService, PingService {

	@Override
	public Order4Get getOrder(String id) {
		Order4Get bean = new Order4Get();
		bean.setName("熔断降级");
		return bean;
	}

	@Override
	public Pong ping(String localAddr) {
		return new Pong(HttpConstants.Ack.ERR.getCode(), HttpConstants.Ack.ERR.getDesc(), localAddr);
	}

}
