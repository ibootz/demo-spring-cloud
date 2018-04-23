package top.bootz.common.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 订阅消息Channel
 * 
 * @author John
 *
 */

public interface MessageSink {

	@Input(MessageChannelConstants.ORDER_TO_PURCHASE_CHANNEL_1)
	SubscribableChannel orderToPurchase1();

	@Input(MessageChannelConstants.ORDER_TO_MALL_CHANNEL_1)
	SubscribableChannel orderToMall1();

	@Input(MessageChannelConstants.ORDER_TO_PURCHASE_REDIRECT_MALL_CHANNEL_1)
	SubscribableChannel orderToPurchase();

	@Input(MessageChannelConstants.PURCHASE_TO_MALL_CHANNEL_1)
	SubscribableChannel purchaseToMall();

}
