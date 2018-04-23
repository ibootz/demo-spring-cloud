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

	@Output(MessageConstants.PURCHASE_TO_MALL_01)
	MessageChannel purchaseToMall();

}
