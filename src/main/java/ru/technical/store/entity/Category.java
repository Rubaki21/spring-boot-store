package ru.technical.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Category {

  @Id
  @ToString.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ToString.Include
  @Column(name = "CATEGORY_NAME", nullable = false, length = 32)
  @Size(min = 1, max = 32, message = "name of category cant be empty or >32 characters long!")
  private String categoryName;

  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
  private Set<Product> products = new HashSet<>();

  public Category(String categoryName) {
    this.categoryName = categoryName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Category category = (Category) o;
    return categoryName.equals(category.categoryName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoryName);
  }
}
