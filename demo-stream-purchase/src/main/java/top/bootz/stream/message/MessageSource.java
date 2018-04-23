package top.bootz.stream.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import top.bootz.common.message.MessageChannelConstants;

/**
 * 向Channel发送消息
 * @author John
 *
 */

public interface MessageSource {

	@Output(MessageChannelConstants.PURCHASE_TO_MALL_CHANNEL_1)
	MessageChannel purchaseToMall();

}
