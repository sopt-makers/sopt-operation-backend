package org.sopt.makers.operation.converter;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;

import java.io.IOException;
import java.util.List;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringListConverter implements AttributeConverter<List<String>, String> {
	private static final ObjectMapper mapper = new ObjectMapper()
		.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
		.configure(FAIL_ON_NULL_FOR_PRIMITIVES, false);

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		try {
			return mapper.writeValueAsString(attribute);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		TypeReference<List<String>> typeReference = new TypeReference<>() {
		};

		try {
			return mapper.readValue(dbData, typeReference);
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}
	}
}
