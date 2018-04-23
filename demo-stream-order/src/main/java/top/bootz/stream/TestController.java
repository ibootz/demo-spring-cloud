package top.bootz.stream;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import top.bootz.common.message.MessaegPayload;
import top.bootz.stream.message.MessageSender;

@RestController
public class TestController {

	@Autowired
	public MessageSender sender;

	@GetMapping("/orderToPurchaseWithToken")
	public void orderToPurchaseWithToken() {
		MessaegPayload message = new MessaegPayload("app_order", "app_purchase", LocalDateTime.now(),
				"test_order_to_purchase");
		Map<String, String> headers = new HashMap<>();
		headers.put("token", UUID.randomUUID().toString());
		sender.orderToPurchase(message, headers);
	}

	@GetMapping("/orderToPurchase")
	public void orderToPurchase() {
		MessaegPayload message = new MessaegPayload("app_order", "app_purchase", LocalDateTime.now(),
				"test_order_to_purchase");
		sender.orderToPurchase(message, new HashMap<String, String>());
	}

	@GetMapping("/orderToMall")
	public void orderToMall() {
		MessaegPayload message = new MessaegPayload("app_order", "app_mall", LocalDateTime.now(), "test_order_to_mall");
		sender.orderToMall(message);
	}

	@GetMapping("/orderToPurchaseRedirectMall")
	public void orderToPurchaseRedirectMall() {
		MessaegPayload message = new MessaegPayload("app_order", "app_purchase", LocalDateTime.now(),
				"test_order_to_purchase_redirect_mall");
		sender.orderToPurchaseRedirectMall(message);
	}

}
