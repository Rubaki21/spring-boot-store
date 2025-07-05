package ru.technical.store.consumer;

import java.time.format.DateTimeFormatter;
import java.util.List;
import ru.technical.store.entity.Category;
import ru.technical.store.entity.Product;
import ru.technical.store.mapper.ProductMapper;
import ru.technical.store.producer.ProductProducer;
import ru.technical.store.service.CategoryService;
import ru.technical.store.service.ProductService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import response.ResponseDto;
import store.ProductDto;

/**
 * kafka consumer, слушает сообщения в топике: out_store.shop_store.products_info
 * и отправляет результат сохранения product's в топик: shop_store.out_store.products_info
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProductConsumer {

  private static final String RESPONSE_TOPIC = "shop_store.out_store.products_info";
  private static  final String PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

  private final ProductMapper productMapper;
  private final ProductService productService;
  private final ProductProducer productProducer;
  private final CategoryService categoryService;

  @KafkaListener(topics = "out_store.shop_store.products_info", groupId = "my-consumer-store-group")
  public void consumeJsonMessage(final List<ProductDto> productDto,
      @Header(KafkaHeaders.RECEIVED_KEY) Integer key,
      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
      @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {
    log.info("Received JSON productDto: {}", productDto);
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
    final String formattedDateTime = LocalDateTime.now().format(formatter);

    for (ProductDto dto : productDto) {
      final Product product = productMapper.convertProductFromDto(dto);
      log.info("Product after mapping: {}", product);
      final ResponseDto response = ResponseDto.newBuilder()
          .setProductInfo(dto.toString())
          .setProcessingTime(formattedDateTime)
          .build();

      try {
        final Category category = categoryService.getCategoryByName(product.getCategoryName());
        product.setCategory(category);

        productService.saveProduct(product);
        log.info("Success save product: {}", product);
        response.setResult(true);
      } catch (Exception e) {
        log.warn("Error save product: {}", e.getMessage());
        response.setExceptionMessage(e.getMessage());
      }
      productProducer.sendMessage(RESPONSE_TOPIC, response);
    }
  }
}
