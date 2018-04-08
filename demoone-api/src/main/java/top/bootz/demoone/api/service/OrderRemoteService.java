package top.bootz.demoone.api.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import top.bootz.demoone.api.dto.Order4Get;

public interface OrderRemoteService {

	@RequestMapping(value = "/orders/{id}", method = RequestMethod.GET)
	public Order4Get getOrder(@PathVariable("id") String id);

}
