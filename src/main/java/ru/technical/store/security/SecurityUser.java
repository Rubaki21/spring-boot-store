package ru.technical.store.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.technical.store.entity.Status;
import ru.technical.store.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class SecurityUser implements UserDetails {

  @Getter
  @Setter
  private User user;
  private final List<SimpleGrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return user.isActive();
  }

  @Override
  public boolean isAccountNonLocked() {
    return user.isActive();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return user.isActive();
  }

  @Override
  public boolean isEnabled() {
    return user.isActive();
  }

  public static UserDetails userConvert(User user) {
    return new org.springframework.security.core.userdetails.User(
        user.getEmail(), user.getPassword(),
        user.getStatus().equals(Status.ACTIVE),
        user.getStatus().equals(Status.ACTIVE),
        user.getStatus().equals(Status.ACTIVE),
        user.getStatus().equals(Status.ACTIVE),
        user.getRole().getGrantedAuthorities()
    );
  }
}
