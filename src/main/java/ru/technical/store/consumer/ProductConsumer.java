package ru.technical.store.consumer;

import static ru.technical.store.util.DateTimeUtil.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import ru.technical.store.entity.Product;
import ru.technical.store.mapper.ProductMapper;
import ru.technical.store.producer.ProductProducer;
import ru.technical.store.service.CategoryService;
import ru.technical.store.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import response.ResponseDto;
import store.ProductAvroDto;

/**
 * kafka consumer, слушает сообщения в topic: out_store.shop_store.products_info и отправляет результат сохранения product's в topic:
 * shop_store.out_store.products_info
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConsumer {

  @Value("${integrations.kafka-topic.producer}")
  private String responseTopic;

  private final ProductMapper productMapper;
  private final ProductService productService;
  private final ProductProducer productProducer;
  private final CategoryService categoryService;

  @KafkaListener(topics = "${integrations.kafka-topic.consumer}", groupId = "${spring.kafka.consumer.group-id}")
  public void consumeJsonMessage(final List<ProductAvroDto> productAvroDto,
      @Header(KafkaHeaders.RECEIVED_KEY) Integer key,
      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {
    log.info("Received JSON productDto: {}", productAvroDto);
    for (ProductAvroDto dto : productAvroDto) {
      final Product product = productMapper.convertProductFromAvroDto(dto);
      log.info("Product after mapping: {}", product);
      final ResponseDto response = ResponseDto.newBuilder()
          .setProductInfo(dto.toString())
          .setProcessingTime(nowDateTime())
          .build();

      try {
        categoryService.fillCategory(product, product.getCategoryName());
        productService.saveProduct(product);
        log.info("Success save product: {}", product);
        response.setResult(true);
      } catch (Exception e) {
        log.warn("Error save product: {}", e.getMessage());
        response.setExceptionMessage(e.getMessage());
      }
      productProducer.sendMessage(responseTopic, response);
    }
  }
}
