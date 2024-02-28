package org.sopt.makers.operation.schedule.converter;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;

public class StringListConverter implements AttributeConverter<List<String>, String> {
	private static final ObjectMapper mapper = new ObjectMapper()
		.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
		.configure(FAIL_ON_NULL_FOR_PRIMITIVES, false);

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		try {
			return mapper.writeValueAsString(attribute);
		} catch (IOException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		TypeReference<List<String>> typeReference = new TypeReference<>() {
		};

		try {
			return mapper.readValue(dbData, typeReference);
		} catch (IOException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}
}
