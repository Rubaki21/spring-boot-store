package ru.technical.store.config;

import ru.technical.store.deserializer.ProductDtoJsonListDeserializer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import store.ProductAvroDto;

/**
 * конфигурация consumer kafka
 */
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

  private final KafkaProperties properties;

  @Bean
  public ConsumerFactory<String, List<ProductAvroDto>> consumerFactory() {
    final Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",", properties.getBootstrapServers()));
    props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getConsumer().getGroupId());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ProductDtoJsonListDeserializer.class.getName());

    return new DefaultKafkaConsumerFactory<>(
        props,
        new StringDeserializer(),
        new ProductDtoJsonListDeserializer()
    );
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, List<ProductAvroDto>> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, List<ProductAvroDto>> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}
