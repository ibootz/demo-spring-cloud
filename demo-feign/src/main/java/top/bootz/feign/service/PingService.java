package top.bootz.feign.service;

import org.springframework.cloud.openfeign.FeignClient;

import top.bootz.demoone.api.service.PingRemoteService;

@FeignClient(name = "demoone", path = "/demo")
public interface PingService extends PingRemoteService {

}
