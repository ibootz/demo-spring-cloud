package top.bootz.stream.message;

import java.time.LocalDateTime;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import top.bootz.common.message.MessaegPayload;
import top.bootz.common.message.MessageChannelConstants;

/**
 * 从Channel接收并处理消息
 * 
 * @author John
 *
 */

@EnableBinding(value = MessageSink.class)
@Component
public class MessageReceiver {

	private static final Logger log = LoggerFactory.getLogger(MessageReceiver.class);

	// purchase接收来自order的消息,不带header标志token
	@StreamListener(target = MessageChannelConstants.ORDER_TO_PURCHASE_CHANNEL_1, condition = "headers['token']==null")
	public void listenOrderToPurchaseWithoutToken(@Payload MessaegPayload payload) {
		log.info("[{}]接收到来自 [{}]的消息:[{}]", payload.getTo(), payload.getFrom(), payload.getContent());
	}

	// Purchase接收来自order的消息,带header标志token
	@StreamListener(target = MessageChannelConstants.ORDER_TO_PURCHASE_CHANNEL_1, condition = "headers['token']!=null")
	public void listenOrderToPurchaseWithToken(@Payload MessaegPayload payload, @Headers Map<String, String> headers,
			@Header String token) {
		log.info("[{}]接收到来自 [{}]的消息:[{}]，token:[{}]", payload.getTo(), payload.getFrom(), payload.getContent(), token);
	}

	// 接收来自purchase转发过来的来自oder的消息
	@StreamListener(target = MessageChannelConstants.ORDER_TO_PURCHASE_REDIRECT_MALL_CHANNEL_1)
	@SendTo(MessageChannelConstants.PURCHASE_TO_MALL_CHANNEL_1)
	public Message<MessaegPayload> listenOrderToPurchaseRedirectMall(@Payload MessaegPayload payload,
			@Headers Map<String, String> headers) {
		log.info("[{}]接收到来自 [{}]的消息:[{}], 并将其转发给[{}]", payload.getTo(), payload.getFrom(), payload.getContent(),
				"app_mall");
		payload = new MessaegPayload(payload.getTo(), "app_mall", LocalDateTime.now(),
				"test_order_to_purchase_redirect_mall");
		return MessageBuilder.withPayload(payload).setHeader("redirect_mall", "true").build();
	}

}
