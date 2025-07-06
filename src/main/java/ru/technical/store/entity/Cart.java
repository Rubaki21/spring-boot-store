package ru.technical.store.entity;

import jakarta.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY)
  private User user;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
  @JoinTable(name = "CART_PRODUCTS",
      joinColumns = @JoinColumn(name = "CART_ID"),
      inverseJoinColumns = @JoinColumn(name = "PRODUCTS_ID"))
  private List<Product> products = new LinkedList<>();

  public Cart(User user, List<Product> products) {
    this.user = user;
    this.products = products;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Cart cart = (Cart) o;
    return products.equals(cart.products);
  }

  @Override
  public int hashCode() {
    return Objects.hash(products);
  }
}
