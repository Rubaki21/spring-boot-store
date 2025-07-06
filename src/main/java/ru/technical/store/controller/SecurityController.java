package ru.technical.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.technical.store.entity.User;
import ru.technical.store.service.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SecurityController {

  private final UserService userService;

  @GetMapping("/login")
  public String login(@RequestParam(value = "error", required = false) Boolean error, Model model) {
    if (error != null) {
      log.warn("Error during authorization");
      model.addAttribute("msg", "Invalid email address or password, or this account does not exist");
    }
    if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
      log.warn("Error during authorization");
      model.addAttribute("msg", "You are already logged in");
      return "error/500";
    }
    return "login";
  }

  @PostMapping("/logout")
  public String logout() {
    return "redirect:/login";
  }

  @GetMapping("/register")
  public String register(Model model) {
    if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
      log.warn("Error during authorization");
      model.addAttribute("msg", "Вы уже авторизованы");
      return "error/500";
    }

    return "register";
  }

  @PostMapping("/register")
  public String registerPost(@Valid @ModelAttribute User user, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn("Binding result has error! " + bindingResult.getFieldError());
      return "register";
    }

    userService.saveUser(user);
    log.info("Register new user with email {} to database", user.getEmail());
    return "redirect:/login";
  }
}
