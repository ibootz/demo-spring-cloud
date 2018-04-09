package top.bootz.feign.service;

import top.bootz.common.constants.HttpConstants;
import top.bootz.feign.dto.Order4Get;
import top.bootz.feign.dto.Pong;

public class DefaultFallback implements OrderService, PingService {

	@Override
	public Order4Get getOrder(String id) {
		Order4Get bean = new Order4Get();
		bean.setName("熔断降级");
		return bean;
	}

	@Override
	public Pong ping() {
		return new Pong(HttpConstants.Ack.ERR.getCode(), HttpConstants.Ack.ERR.getDesc(), "");
	}

}
