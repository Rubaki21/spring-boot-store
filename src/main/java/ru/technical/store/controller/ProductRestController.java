package ru.technical.store.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.technical.store.entity.Product;

import java.util.List;
import ru.technical.store.mapper.ProductMapper;
import ru.technical.store.service.CategoryService;
import ru.technical.store.service.ProductService;
import store.ProductDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("external/products/")
public class ProductRestController {

  private final RestTemplate restTemplate;
  private final ProductMapper productMapper;
  private final ProductService productService;
  private final CategoryService categoryService;

  @Value("${integrations.external.product-service.url}")
  private String externalProductApi;

  @SneakyThrows
  @GetMapping("/fetch")
  public void fetchExternalProducts(HttpServletResponse response) {
    log.info("Fetching products from external API: {}", externalProductApi);
    final List<ProductDto> products = restTemplate
        .exchange(
            externalProductApi,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ProductDto>>() {
            }
        )
        .getBody();

    for (ProductDto productDto : products) {
      log.info("Received JSON productDto: {}", productDto);
      final Product product = productMapper.convertProductFromDto(productDto);
      log.info("Product after mapping: {}", product);
      categoryService.fillCategory(product, product.getCategoryName());
      productService.saveProduct(product);
    }

    response.sendRedirect("/admin");
  }

  @GetMapping("findAll")
  public String findAllExternalProducts() {
    log.info("Find all products from external API: {}", externalProductApi);
    return """
        [
          { "title": "товар 1", "price": "21", "categoryName": "CPU", "quantity":"7" },
          { "title": "товар 2", "price": "22.3", "categoryName": "CPU" }
        ]
        """;
  }
}
