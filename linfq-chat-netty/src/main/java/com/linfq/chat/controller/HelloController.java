package com.linfq.chat.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloController.
 *
 * @author linfq
 * @date 2019/7/21 16:36
 */
@RestController
public class HelloController {

	@GetMapping("/hello")
	public String hello() {
		return "hello muxin~~";
	}
}
