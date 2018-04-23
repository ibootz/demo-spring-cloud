package top.bootz.stream.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

import top.bootz.common.message.MessageConstants;

/**
 * 订阅消息Channel
 * 
 * @author John
 *
 */

public interface MessageSink {

	@Input(MessageConstants.ORDER_TO_PURCHASE_01)
	SubscribableChannel orderToPurchase1();

	@Input(MessageConstants.ORDER_TO_PURCHASE_REDIRECT_MALL_01)
	SubscribableChannel orderToPurchase();

}
