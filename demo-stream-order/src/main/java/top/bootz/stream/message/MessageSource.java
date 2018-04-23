package top.bootz.stream.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import top.bootz.common.message.MessageConstants;

/**
 * 向Channel发送消息
 * @author John
 *
 */

public interface MessageSource {

	@Output(MessageConstants.ORDER_TO_PURCHASE_01)
	MessageChannel orderToPurchase1();

	@Output(MessageConstants.ORDER_TO_MALL_01)
	MessageChannel orderToMall1();

	@Output(MessageConstants.ORDER_TO_PURCHASE_REDIRECT_MALL_01)
	MessageChannel orderToPurchaseRedirectMall1();

}
