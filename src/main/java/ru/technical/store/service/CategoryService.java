package ru.technical.store.service;

import ru.technical.store.entity.Category;
import ru.technical.store.entity.Product;
import ru.technical.store.exception.CategoryIsNotEmptyException;
import ru.technical.store.exception.CategoryNotFoundException;
import ru.technical.store.repository.CategoryRepository;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public void saveCategory(Category category) {
    categoryRepository.save(category);
  }

  public Category getCategoryById(Long id) {
    return categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
  }

  public Category getCategoryByName(String name) {
    return categoryRepository.findCategoryByCategoryName(name)
        .orElseThrow(() -> new NoSuchElementException(String.format("в базе данных отсутствует категория с именем: %s", name)));
  }

  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  public void deleteById(Long id) {
      if (!getCategoryById(id).getProducts().isEmpty()) {
          throw new CategoryIsNotEmptyException(id); // Категория не может быть удалена, если в ней есть товары
      }
    categoryRepository.deleteById(id);
  }

  public void fillCategory(Product product, String categoryName) {
    product.setCategory(getCategoryByName(product.getCategoryName()));
  }
}
