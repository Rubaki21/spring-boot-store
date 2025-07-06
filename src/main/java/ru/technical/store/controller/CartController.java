package ru.technical.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.technical.store.entity.Cart;
import ru.technical.store.entity.Product;
import ru.technical.store.service.CartService;
import ru.technical.store.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Slf4j
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
public class CartController {

  private final ProductService productService;
  private final CartService cartService;

  @GetMapping
  public String cartView(Model model) {
    Cart cart = cartService.getLoggedUserCart();
    BigDecimal totalPrice = cartService.getTotalPriceCart();

    model.addAttribute("cart", cart.getProducts());
    model.addAttribute("totalPrice", totalPrice);

    log.info("Redirect to /cart page");
    return "cart";
  }

  @PostMapping("/new/{id}")
  public String addProdToCart(@PathVariable("id") Long id) {
    Product product = productService.getProductById(id);
    cartService.addProductToCart(product);

    log.info("Add product with id {} to cart", product.getId());
    return "redirect:/index";
  }

  @PostMapping("/delete/{id}")
  public String deleteProductFromCart(@PathVariable("id") Long id) {
    Product product = productService.getProductById(id);
    cartService.deleteProductFromCart(product);

    log.info("Delete product with id {} from cart", product.getId());
    return "redirect:/cart";
  }
}
