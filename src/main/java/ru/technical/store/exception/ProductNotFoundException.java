package ru.technical.store.exception;

public class ProductNotFoundException extends RuntimeException {

  public ProductNotFoundException(Long id) {
    super(String.format("Product with id '%s' not found", id));
  }
}
