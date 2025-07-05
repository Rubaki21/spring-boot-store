package ru.technical.store.deserializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import store.ProductDto;

import java.util.List;

public class ProductDtoJsonListDeserializer implements Deserializer<List<ProductDto>> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public List<ProductDto> deserialize(String topic, byte[] data) {
    if (data == null || data.length == 0) {
      return null;
    }

    try {
      return objectMapper.readValue(data, new TypeReference<>() {
      });
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to deserialize JSON to List<ProductDto>", e);
    }
  }
}
