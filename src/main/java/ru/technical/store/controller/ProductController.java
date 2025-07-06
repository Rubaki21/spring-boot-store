package ru.technical.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.technical.store.entity.Category;
import ru.technical.store.entity.Product;
import ru.technical.store.service.CategoryService;
import ru.technical.store.service.ProductService;
import ru.technical.store.service.ReviewService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductController {

  private final CategoryService categoryService;
  private final ProductService productService;
  private final ReviewService reviewService;


  @GetMapping("/new")
  public String createProduct(Model model) {
    List<Category> categoryList = categoryService.getAllCategories();
    model.addAttribute("categoryList", categoryList);
    return "createProd";
  }

  @PostMapping("/new")
  public String createProduct(@Valid @ModelAttribute Product product, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn("Binding result has error! " + bindingResult.getFieldError());
      return "createProd";
    }

    productService.saveProduct(product);
    log.info("Save new product with name {} to database", product.getTitle());
    return "redirect:/admin";
  }

  @GetMapping("/edit/{id}")
  public String editProduct(@PathVariable("id") Long id, Model model) {
    Product product = productService.getProductById(id);
    List<Category> categoryList = categoryService.getAllCategories();

    model.addAttribute("categoryList", categoryList);
    model.addAttribute("productForm", product);

    return "editProd";
  }

  @PostMapping("/edit/{id}")
  public String editProduct(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      log.warn("Binding result has error! " + bindingResult.getFieldError());
      model.addAttribute("productForm", product);
      return "editProd";
    }

    productService.saveProduct(product);
    log.info("Edit product with id {}", product.getId());
    return "redirect:/admin";
  }

  @PostMapping("/delete/{id}")
  public String deleteProduct(@PathVariable("id") Long id) {
    productService.deleteProductById(id);
    log.info("Delete product with id {} from database", id);
    return "redirect:/admin";
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('USER')")
  public String getOneProduct(@PathVariable("id") Long id, Model model) {
    model.addAttribute("product", productService.getProductById(id));
    model.addAttribute("reviews", reviewService.getReviewsByProductId(id));
    log.info("Get product with id {} and it reviews from database", id);
    return "product";
  }
}
