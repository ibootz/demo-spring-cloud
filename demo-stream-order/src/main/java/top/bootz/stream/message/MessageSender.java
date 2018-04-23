package top.bootz.stream.message;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import top.bootz.common.message.MessaegPayload;

@EnableBinding(value = { MessageSource.class })
public class MessageSender {

	@Autowired
	private MessageSource messageSource;

	public void orderToPurchase(MessaegPayload message, Map<String, String> headers) {
		messageSource.orderToPurchase1().send(MessageBuilder.withPayload(message).copyHeaders(headers).build(), 10000);
	}

	public void orderToMall(MessaegPayload message) {
		messageSource.orderToMall1().send(MessageBuilder.withPayload(message).build(), 10000);
	}

	public void orderToPurchaseRedirectMall(MessaegPayload message) {
		messageSource.orderToPurchaseRedirectMall1()
				.send(MessageBuilder.withPayload(message).setHeader("redirect_mall", true).build(), 10000);
	}

}
