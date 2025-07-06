package ru.technical.store.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import response.ResponseDto;

/**
 * kafka producer, отправляет сообщения в топик: shop_store.out_store.products_info
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(String topicName, ResponseDto responseDto) {
    log.info("Start sending: {}", responseDto);
    kafkaTemplate.send(topicName, responseDto.toString());
    log.info("End sending");
  }
}
