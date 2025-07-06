package ru.technical.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.technical.store.entity.Category;
import ru.technical.store.entity.Product;
import ru.technical.store.entity.User;
import ru.technical.store.service.CategoryService;
import ru.technical.store.service.ProductService;
import ru.technical.store.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminPanelController {

  private final CategoryService categoryService;
  private final ProductService productService;
  private final UserService userService;

  @GetMapping
  public String adminPageController(Model model) {
    List<Category> categoryList = categoryService.getAllCategories();
    List<Product> productList = productService.getAllProducts();
    List<User> userList = userService.getAllUsers();

    model.addAttribute("categoryList", categoryList);
    model.addAttribute("productList", productList);
    model.addAttribute("userList", userList);

    log.info("Redirect to admin panel");
    return "admin";
  }
}
