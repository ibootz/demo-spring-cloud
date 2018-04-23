package top.bootz.stream;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import top.bootz.stream.message.MessaegPayload;
import top.bootz.stream.message.MessageSender;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoStreamApplicationTests {

	@Autowired
	public MessageSender sender;

	@Test
	public void orderToPurchase() {
		MessaegPayload message = new MessaegPayload("app_order", "app_purchase", LocalDateTime.now(),
				"test_order_to_purchase");
		sender.order2purchase1().send(MessageBuilder.withPayload(message).build(), 10000);
	}

	// @Test
	public void orderToMall() {
		MessaegPayload message = new MessaegPayload("app_order", "app_mall", LocalDateTime.now(), "test_order_to_mall");
		sender.order2purchase1().send(MessageBuilder.withPayload(message).build(), 10000);
	}

	// @Test
	public void orderRedirectMall() {
		MessaegPayload message = new MessaegPayload("app_order", "app_purchase_redirect_app_mall", LocalDateTime.now(),
				"test_order_to_mall");
		sender.order2purchase1().send(MessageBuilder.withPayload(message).setHeader("redirect_mall", true).build(),
				10000);
	}

}
