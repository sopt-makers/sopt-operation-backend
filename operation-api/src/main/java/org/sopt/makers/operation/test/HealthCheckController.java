package org.sopt.makers.operation.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class HealthCheckController {

	@GetMapping
	public String healthCheck() {
		return "Hello Operation!";
	}
}
