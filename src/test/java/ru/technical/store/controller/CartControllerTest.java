package ru.technical.store.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.technical.store.entity.Cart;
import ru.technical.store.entity.Category;
import ru.technical.store.entity.Product;
import ru.technical.store.service.CartService;
import ru.technical.store.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(authorities = "USER")
@AutoConfigureMockMvc(addFilters = false)
@MockitoBean(types = UserDetailsService.class)
@WebMvcTest(controllers = CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private ProductService productService;

    private final Long id = 1L;

    @Test
    void cartView() throws Exception {
        Cart cart = new Cart();
        when(cartService.getLoggedUserCart()).thenReturn(cart);

        this.mockMvc.perform(get("/cart"))
                .andExpectAll(
                        status().isOk(),
                        model().attribute("cart", cart.getProducts()),
                        model().attribute("totalPrice", cartService.getTotalPriceCart()),
                        view().name("cart")
                );
    }

    @Test
    void addProdToCart() throws Exception {
        Product product = new Product("LG", "test", new Category("smart"), new BigDecimal(1000), 0);
        when(productService.getProductById(id)).thenReturn(product);

        this.mockMvc.perform(post("/cart/new/1"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/index")
                );

        verify(cartService, times(1)).addProductToCart(product);
        verify(productService, times(1)).getProductById(id);
    }

    @Test
    void deleteProductFromCart() throws Exception {
        Product product = new Product("LG", "test", new Category("smart"), new BigDecimal(1000), 0);
        when(productService.getProductById(id)).thenReturn(product);

        this.mockMvc.perform(post("/cart/delete/1"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/cart")
                );

        verify(cartService, times(1)).deleteProductFromCart(product);
        verify(productService, times(1)).getProductById(id);
    }
}