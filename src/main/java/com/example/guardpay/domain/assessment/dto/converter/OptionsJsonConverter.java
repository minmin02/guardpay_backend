package com.example.guardpay.domain.assessment.dto.converter;

// OptionsJsonConverter.java
import com.example.guardpay.domain.assessment.dto.OptionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter // 이 클래스가 JPA 컨버터임을 선언
public class OptionsJsonConverter implements AttributeConverter<List<OptionDto>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 자바 객체(List<OptionDto>) -> DB 컬럼(JSON 문자열)
    @Override
    public String convertToDatabaseColumn(List<OptionDto> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            // 예외 처리 로직 (e.g., 로깅)
            throw new RuntimeException("Could not convert list to JSON string", e);
        }
    }

    // DB 컬럼(JSON 문자열) -> 자바 객체(List<OptionDto>)
    @Override
    public List<OptionDto> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<List<OptionDto>>() {});
        } catch (IOException e) {
            // 예외 처리 로직
            throw new RuntimeException("Could not convert JSON string to list", e);
        }
    }
}