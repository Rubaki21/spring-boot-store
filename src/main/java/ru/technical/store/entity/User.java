package ru.technical.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "USERS")
public class User {

  @Id
  @ToString.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ToString.Include
  @Column(name = "EMAIL", nullable = false)
  @Email(message = "this is not email!")
  @Size(min = 1, max = 255, message = "too long email (or empty!)")
  private String email;

  @NotNull
  @ToString.Include
  @Column(name = "FIRST_NAME", nullable = false, length = 100)
  @Size(min = 1, max = 100, message = "firstname can not be empty or >100 characters long!")
  private String firstname;

  @NotNull
  @ToString.Include
  @Column(name = "LAST_NAME", nullable = false, length = 100)
  @Size(min = 1, max = 100, message = "lastname can not be empty or >100 characters long!")
  private String lastname;

  @NotNull
  @ToString.Include
  @Column(name = "PASSWORD", nullable = false)
  private String password;

  @NotNull
  @ToString.Include
  @Enumerated(value = EnumType.STRING)
  @Column(name = "ROLE", nullable = false, length = 25)
  private Role role = Role.USER;

  @NotNull
  @ToString.Include
  @Enumerated(value = EnumType.STRING)
  @Column(name = "STATUS", nullable = false, length = 25)
  private Status status = Status.ACTIVE;

  @NotNull
  @ToString.Exclude
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "CART_ID", referencedColumnName = "ID", nullable = false)
  private Cart cart = new Cart();

  @ToString.Exclude
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "author")
  private List<Review> comments;

  public User(String email, String firstname, String lastname, String password, Role role, Status status) {
    this.email = email;
    this.firstname = firstname;
    this.lastname = lastname;
    this.password = password;
    this.role = role;
    this.status = status;
  }

  public boolean isActive() {
    return this.getStatus().equals(Status.ACTIVE);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return email.equals(user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email);
  }
}
