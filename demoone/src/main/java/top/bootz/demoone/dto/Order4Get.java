package top.bootz.demoone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order4Get {

	private String id;

	private double price;

	private int count;

	private String name;

}
