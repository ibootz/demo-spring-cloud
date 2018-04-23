package top.bootz.stream.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import top.bootz.common.message.MessaegPayload;

@EnableBinding(value = { MessageSource.class })
public class MessageSender {

	@Autowired
	private MessageSource messageSource;

	public void purchaseToMall(MessaegPayload payload) {
		messageSource.purchaseToMall().send(MessageBuilder.withPayload(payload).build(), 10000);
	}

}
