package org.operation.common.util;

import static java.util.Objects.*;

import java.security.Principal;

import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonUtils {
	public Long getMemberId(Principal principal) {
		return nonNull(principal) ? Long.valueOf(principal.getName()) : null;
	}
}
