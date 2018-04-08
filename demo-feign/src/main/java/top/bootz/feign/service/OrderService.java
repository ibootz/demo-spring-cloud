package top.bootz.feign.service;

import org.springframework.cloud.openfeign.FeignClient;

import top.bootz.demoone.api.service.OrderRemoteService;

@FeignClient(name = "demoone", path = "/demo")
public interface OrderService extends OrderRemoteService {

}
