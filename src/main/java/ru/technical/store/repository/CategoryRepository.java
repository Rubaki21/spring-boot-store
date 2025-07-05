package ru.technical.store.repository;

import ru.technical.store.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Optional<Category> findCategoryByCategoryName(String categoryName);
}
