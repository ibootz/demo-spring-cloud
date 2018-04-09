package top.bootz.feign.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pong {

	private String ack;

	private String desc;

	private String localAddr;

}
