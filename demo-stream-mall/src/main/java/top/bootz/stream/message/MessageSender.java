package top.bootz.stream.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import top.bootz.common.message.MessageChannelConstants;

/**
 * 向Channel发送消息
 * @author John
 *
 */

public interface MessageSender {

	@Output(MessageChannelConstants.ORDER_TO_PURCHASE_CHANNEL_1)
	MessageChannel orderToPurchase1();

	@Output(MessageChannelConstants.ORDER_TO_MALL_CHANNEL_1)
	MessageChannel orderToMall1();
	
	@Output(MessageChannelConstants.ORDER_TO_PURCHASE_REDIRECT_MALL_CHANNEL_1)
	MessageChannel orderToPurchaseRedirectMall1();
	
	@Output(MessageChannelConstants.PURCHASE_TO_MALL_CHANNEL_1)
	MessageChannel purchaseToMall1();

}
