package top.bootz.demoone.web.controller;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import top.bootz.demoone.dto.Order4Get;

@RestController
public class OrderController {

	@RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
	public Order4Get getOrder(@PathVariable("id") String id) {
		return new Order4Get(id, RandomUtils.nextDouble() * 10, 22, "订单_" + id);
	}

}
