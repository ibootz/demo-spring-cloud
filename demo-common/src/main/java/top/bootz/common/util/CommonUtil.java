package top.bootz.common.util;

public class CommonUtil {

	private CommonUtil() {

	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
