package org.sopt.makers.operation.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	@GetMapping("/")
	public String healthCheck() {
		return "Hello Operation!";
	}
}
