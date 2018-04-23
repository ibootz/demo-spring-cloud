package top.bootz.stream.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageSender {

	@Output(MessageChannelConstants.ORDER_TO_PURCHASE_CHANNEL_1)
	MessageChannel order2purchase1();

	@Output(MessageChannelConstants.ORDER_TO_MALL_CHANNEL_1)
	MessageChannel order2mall1();

}
