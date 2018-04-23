package top.bootz.common.message;

public interface MessageConstants {

	// 订单微应用到采购微应用的消息通道1
	String ORDER_TO_PURCHASE_01 = "order_to_purchase_01";

	// 订单微应用到商城微应用的消息通道1
	String ORDER_TO_MALL_01 = "order_to_mall_01";

	// 采购微应用到商城微应用的消息通道1
	String PURCHASE_TO_MALL_01 = "purchase_to_mall_01";

	// 订单微应用到采购微应用，再转发到商城微应用的消息通道1
	String ORDER_TO_PURCHASE_REDIRECT_MALL_01 = "order_to_purchase_redirect_mall_01";

}
