package top.bootz.common.message;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 具体消息实体
 * @author John
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessaegPayload {

	private String from;

	private String to;

	private LocalDateTime localDateTime;

	private @NonNull String content;

}
