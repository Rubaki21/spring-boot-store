package ru.technical.store.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.technical.store.entity.Product;
import ru.technical.store.entity.Review;
import ru.technical.store.entity.User;
import ru.technical.store.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(authorities = "USER")
@AutoConfigureMockMvc(addFilters = false)
@MockitoBean(types = UserDetailsService.class)
@WebMvcTest(controllers = ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @Test
    void addComment() throws Exception {
        Review review = new Review("test", new User(), new Product(), 4);
        // Тесты POST-запросов
        this.mockMvc.perform(post("/comments/new")
                        .flashAttr("review", review))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/index"));

        verify(reviewService, times(1)).saveReview(review);

        review.setRating(-1); // Делаем отзыв некорректным (отрицательная оценка)
        this.mockMvc.perform(post("/comments/new")
                        .flashAttr("review", review))
                .andExpect(model().hasErrors()); // И получаем ошибку в Binding Result
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteComment() throws Exception {
        this.mockMvc.perform(post("/comments/delete/1"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        view().name("redirect:/index")
                );

        Long id = 1L;
        verify(reviewService, times(1)).deleteReviewById(id);
    }
}