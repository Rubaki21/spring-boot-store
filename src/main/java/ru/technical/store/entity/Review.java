package ru.technical.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "REVIEWS")
public class Review {

  @Id
  @ToString.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ToString.Include
  @Column(name = "COMMENT", length = 384, nullable = false)
  @Size(max = 384, message = "comment can not be >384 characters long!")
  private String comment;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = false)
  private User author;

  @NotNull
  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID", nullable = false)
  private Product product;

  @NotNull
  @ToString.Include
  @Column(name = "RATING", nullable = false)
  @Min(value = 1, message = "minimal rating is 1!")
  @Max(value = 5, message = "maximal rating is 5!")
  private int rating;

  public Review(String comment, User author, Product product, int rating) {
    this.comment = comment;
    this.author = author;
    this.product = product;
    this.rating = rating;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Review review = (Review) o;
    return comment.equals(review.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(comment);
  }
}
