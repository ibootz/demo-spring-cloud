package top.bootz.stream.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 定义消息发送渠道
 * @author John
 *
 */

public interface MessageSink {

	@Input(MessageChannelConstants.ORDER_TO_PURCHASE_CHANNEL_1)
	SubscribableChannel order2purchase1();

	@Input(MessageChannelConstants.ORDER_TO_MALL_CHANNEL_1)
	SubscribableChannel order2mall1();

}
