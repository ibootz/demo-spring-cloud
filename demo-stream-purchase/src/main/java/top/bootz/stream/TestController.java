package top.bootz.stream;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import top.bootz.common.message.MessaegPayload;
import top.bootz.stream.message.MessageSender;

@RestController
public class TestController {

	@Autowired
	public MessageSender sender;

	@GetMapping("/orderToPurchaseRedirectMall")
	public void orderToPurchaseRedirectMall() {
		MessaegPayload message = new MessaegPayload("app_purchase", "app_mall", LocalDateTime.now(),
				"test_purchase_to_mall");
		sender.purchaseToMall(message);
	}

}
