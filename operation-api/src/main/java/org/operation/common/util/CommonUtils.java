package org.operation.common.util;

import java.net.URI;
import java.security.Principal;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.NonNull;

@Configuration
public class CommonUtils {
	public long getMemberId(@NonNull Principal principal) {
		return Long.parseLong(principal.getName());
	}

	public URI getURI(String path, long id) {
		return ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path(path)
				.buildAndExpand(id)
				.toUri();
	}
}
