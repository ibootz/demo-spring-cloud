package top.bootz.ribbon.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import top.bootz.common.constants.HttpConstants;

@Service
public class ConsumerService {

	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod = "errorFallback", groupKey = "demoone", commandKey = "ping")
	public String ping() {
		return restTemplate.getForEntity("http://DEMOONE/demo/ping", String.class).getBody();
	}

	@HystrixCommand(fallbackMethod = "errorFallback", groupKey = "demoone", commandKey = "getOrder")
	public String getOrder() {
		String uuid = UUID.randomUUID().toString();
		String url = String.format("http://DEMOONE/demo/orders/%s", uuid);
		return restTemplate.getForEntity(url, String.class).getBody();
	}

	public String errorFallback() {
		JsonObject json = new JsonObject();
		json.addProperty("ack", HttpConstants.Ack.ERR.getCode());
		json.addProperty("desc", "调用失败，降级处理");
		return json.toString();
	}

}
