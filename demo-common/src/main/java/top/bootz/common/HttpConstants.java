package top.bootz.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Http相关常量
 * @author John
 *
 */
public final class HttpConstants {

	private HttpConstants() {
	};

	@Getter
	@AllArgsConstructor
	public enum Ack {

		OK("OK", "成功"),

		ERR("ERR", "失败");

		private final String code;

		private final String desc;

	}

}
