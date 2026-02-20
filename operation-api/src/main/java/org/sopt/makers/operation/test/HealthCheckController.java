package org.sopt.makers.operation.test;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.client.auth.AuthClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class HealthCheckController {
    private final AuthClient authClient;
	@GetMapping
	public String healthCheck() {
		return "Hello Operation!!!!";
	}

    @GetMapping("/auth-client")
    public ResponseEntity<Map<String, Object>> testAuthClient() {
        try {
            String jwk = authClient.getJwk();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "AuthClient is working!",
                    "jwkLength", jwk.length(),
                    "jwkPreview", jwk.substring(0, Math.min(100, jwk.length())) + "..."
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

}
