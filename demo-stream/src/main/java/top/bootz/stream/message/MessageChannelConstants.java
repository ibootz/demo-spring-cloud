package top.bootz.stream.message;

public interface MessageChannelConstants {

	// 订单微应用到采购微应用的消息通道1
	String ORDER_TO_PURCHASE_CHANNEL_1 = "order_to_purchase_channel_1";

	// 订单微应用到商城微应用的消息通道1
	String ORDER_TO_MALL_CHANNEL_1 = "order_to_mall_channel_1";

	// 采购微应用到商城微应用的消息通道1
	String PURCHASE_TO_MALL_CHANNEL_1 = "purchase_to_mall_channel_1";

	// 订单微应用到采购微应用，再转发到商城微应用的消息通道1
	String ORDER_TO_PURCHASE_REDIRECT_MALL_CHANNEL_1 = "order_to_purchase_redirect_mall_channel_1";

}
