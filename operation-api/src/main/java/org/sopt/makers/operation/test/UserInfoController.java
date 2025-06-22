package org.sopt.makers.operation.test;


import lombok.RequiredArgsConstructor;
import org.sopt.makers.operation.authentication.MakersAuthentication;
import org.sopt.makers.operation.client.auth.AuthClient;
import org.sopt.makers.operation.client.auth.AuthClientProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/me")
@RequiredArgsConstructor
public class UserInfoController {

    private final AuthClientProperty authProperty;
    private final AuthClient authClient;

    @GetMapping
    public void getCurrentUserInfo(Authentication authentication) {
        MakersAuthentication makers = (MakersAuthentication) authentication;
        Map<String, Object> response = Map.of(
                "userId", makers.getUserId(),
                "roles", makers.getRoles()
        );
        System.out.println();
    }

}

