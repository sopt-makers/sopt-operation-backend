package org.sopt.makers.operation.common.util;

import java.util.List;
import java.util.Arrays;
import java.net.URLEncoder;
import java.security.Principal;
import java.io.UnsupportedEncodingException;

import org.sopt.makers.operation.code.failure.UserFailureCode;
import org.sopt.makers.operation.exception.ParameterDecodeCustomException;
import org.springframework.context.annotation.Configuration;

import lombok.NonNull;

@Configuration
public class CommonUtils {

	private static final String DECODING_CHARSET = "UTF-8";
	private static final String DELIMITER_ID_PARAMETER = ",";

	public long getMemberId(@NonNull Principal principal) {
		return Long.parseLong(principal.getName());
	}

	public List<Long> getIdsFromParameter(@NonNull String parameter)  {
		try {
			String encodedParameter = URLEncoder.encode(parameter, DECODING_CHARSET);
			return Arrays.stream(encodedParameter.split(DELIMITER_ID_PARAMETER))
					.map(id -> Long.parseLong(id.trim()))
					.toList();
		} catch (UnsupportedEncodingException ex) {
			throw new ParameterDecodeCustomException(UserFailureCode.INVALID_PARAMETER, parameter);
		}
	}
}
