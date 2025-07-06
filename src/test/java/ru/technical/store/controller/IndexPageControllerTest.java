package ru.technical.store.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.technical.store.entity.Product;
import ru.technical.store.service.CategoryService;
import ru.technical.store.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(authorities = "USER")
@AutoConfigureMockMvc(addFilters = false)
@MockitoBean(types = UserDetailsService.class)
@WebMvcTest(controllers = IndexPageController.class)
class IndexPageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ProductService productService;

  @MockitoBean
  private CategoryService categoryService;

  @Test
  void homepage() throws Exception {
    int page = 1, pageSize = 3;
    when(productService.getPaginatedProducts(page, pageSize)).thenReturn(Page.empty()); // Mock страницы (Page)

    this.mockMvc.perform(get("/"))
        .andExpectAll(
            status().isOk(),
            view().name("index")
        );

    //verify(v, times(1)).getAllByCategoryAndTitle(category, title);
  }

  @Test
  void homepageSearchByTitle() throws Exception {
    String title = "title", category = "category";

    this.mockMvc.perform(get("/search")
            .param("category", category)
            .param("title", title))
        .andExpectAll(
            status().isOk(),
            model().attribute("productListSearched", List.of()),
            view().name("search")
        );

    verify(productService, times(1)).getAllByCategoryAndTitle(category, title);
  }

  @Test
  void viewPaginated() throws Exception {
    int page = 1, pageSize = 3;
    when(productService.getPaginatedProducts(page, pageSize)).thenReturn(Page.empty()); // Mock страницы (Page)
    Page<Product> paginatedProducts = productService.getPaginatedProducts(page, pageSize);

    this.mockMvc.perform(get("/" + page))
        .andExpectAll(
            status().isOk(),
            model().attribute("pageNo", page),
            model().attribute("totalPages", paginatedProducts.getTotalPages()),
            model().attribute("productPaginated", paginatedProducts.getContent()),
            model().attribute("categoryList", categoryService.getAllCategories()),
            view().name("index")
        );

  }
}