package org.operation.common.util;

import java.security.Principal;

import org.springframework.context.annotation.Configuration;

import lombok.NonNull;

@Configuration
public class CommonUtils {
	public long getMemberId(@NonNull Principal principal) {
		return Long.parseLong(principal.getName());
	}
}
