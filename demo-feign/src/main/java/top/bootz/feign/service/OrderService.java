package top.bootz.feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import top.bootz.feign.dto.Order4Get;

@FeignClient(name = "demoone", path = "/demo")
public interface OrderService {

	@RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
	public Order4Get getOrder(@PathVariable("id") String id);

}
