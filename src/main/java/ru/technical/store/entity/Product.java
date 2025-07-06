package ru.technical.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "PRODUCTS")
public class Product {

  @Id
  @ToString.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ToString.Include
  @Column(name = "TITLE", nullable = false)
  @Size(min = 1, max = 255, message = "title can not be empty or >255 characters long!")
  private String title;

  @ToString.Include
  @Column(name = "IMAGE_LOCATION", length = 384)
  @Size(max = 384, message = "title can not be empty or >384 characters long!")
  private String imageLocation; // Картинки в БД хранятся в виде ссылок из интернета

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID")
  private Category category;

  @NotNull
  @ToString.Include
  @Column(name = "PRICE", nullable = false)
  @Min(value = 0, message = "price cant be less than 0!")
  private BigDecimal price;

  @ToString.Include
  @Min(value = 0, message = "quantity cant be less than 0!")
  private int quantity = 0;

  @ToString.Exclude
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.REMOVE)
  private List<Review> reviews;

  @ToString.Exclude
  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
  private List<Cart> productInCartsList;

    public Product(String title, String imageLocation, Category category, BigDecimal price, int quantity) {
        this.title = title;
        this.imageLocation = imageLocation;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

  public void addCommentToProduct(Review review) {
    reviews.add(review);
  }

    public String getCategoryName() {
        return category.getCategoryName();
    }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Product product = (Product) o;
    return title.equals(product.title);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title);
  }
}
