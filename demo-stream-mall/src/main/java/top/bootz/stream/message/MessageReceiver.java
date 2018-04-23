package top.bootz.stream.message;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import top.bootz.common.message.MessaegPayload;
import top.bootz.common.message.MessageConstants;

/**
 * 从Channel接收并处理消息
 * 
 * @author John
 *
 */

@EnableBinding(value = MessageSink.class)
public class MessageReceiver {

	private static final Logger log = LoggerFactory.getLogger(MessageReceiver.class);

	// mall接收来自order的消息
	@StreamListener(target = MessageConstants.ORDER_TO_MALL_01)
	public void listenOrderToMall(@Payload MessaegPayload payload, @Headers Map<String, String> headers) {
		log.info("[{}]接收到来自 [{}]的消息:[{}]", payload.getTo(), payload.getFrom(), payload.getContent());
	}

	// mall接收来自purchase的消息
	@StreamListener(target = MessageConstants.PURCHASE_TO_MALL_01)
	public void listenPurchaseToMall(@Payload MessaegPayload payload, @Headers Map<String, String> headers) {
		log.info("[{}]接收到来自 [{}]的消息:[{}]", payload.getTo(), payload.getFrom(), payload.getContent());
		log.info("header['redirect_mall'] = {}", headers.get("redirect_mall"));
	}

}
