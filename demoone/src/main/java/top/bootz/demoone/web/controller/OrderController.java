package top.bootz.demoone.web.controller;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import top.bootz.demoone.api.dto.Order4Get;
import top.bootz.demoone.api.service.OrderRemoteService;

@RestController
public class OrderController implements OrderRemoteService {

	@Override
	public Order4Get getOrder(@PathVariable("id") String id) {

		long sleepTime = RandomUtils.nextInt(3000);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("id [" + id + "] sleepTime [" + sleepTime + "]");

		return new Order4Get(id, RandomUtils.nextDouble() * 10, 22, "订单_" + id);
	}

}
