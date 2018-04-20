package top.bootz.demoone.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class AppFromController {

	@Value("${app.from:default_name}")
	private String from;

	@GetMapping(value = "/from")
	public String getFrom() {
		return from;
	}

}
