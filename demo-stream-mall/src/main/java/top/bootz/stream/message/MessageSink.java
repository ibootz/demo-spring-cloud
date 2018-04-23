package top.bootz.stream.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

import top.bootz.common.message.MessageChannelConstants;

/**
 * 订阅消息Channel
 * 
 * @author John
 *
 */

public interface MessageSink {

	@Input(MessageChannelConstants.ORDER_TO_MALL_CHANNEL_1)
	SubscribableChannel orderToMall1();

	@Input(MessageChannelConstants.PURCHASE_TO_MALL_CHANNEL_1)
	SubscribableChannel purchaseToMall();

}
