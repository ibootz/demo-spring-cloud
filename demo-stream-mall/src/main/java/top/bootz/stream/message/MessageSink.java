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

	@Input(MessageConstants.ORDER_TO_MALL_01)
	SubscribableChannel orderToMall1();

	@Input(MessageConstants.PURCHASE_TO_MALL_01)
	SubscribableChannel purchaseToMall();

}
