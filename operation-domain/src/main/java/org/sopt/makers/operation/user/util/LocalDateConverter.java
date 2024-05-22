package org.sopt.makers.operation.user.util;

import jakarta.persistence.Converter;
import jakarta.persistence.AttributeConverter;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@Converter
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {
    @Override
    public Date convertToDatabaseColumn(LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(Date::valueOf)
                .orElse(null);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
        return Optional.ofNullable(date)
                .map(Date::toLocalDate)
                .orElse(null);
    }
}
