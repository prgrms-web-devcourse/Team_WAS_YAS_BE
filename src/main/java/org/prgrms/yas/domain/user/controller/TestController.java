package org.prgrms.yas.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@GetMapping("/nginx/check")
	public String nginxCheck(){
		return "nginx version 0.1";
	}
}
