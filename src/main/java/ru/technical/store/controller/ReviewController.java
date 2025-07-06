package ru.technical.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.technical.store.entity.Review;
import ru.technical.store.service.ReviewService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
@PreAuthorize("hasAuthority('USER')")
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping("/new")
  public String addComment(@Valid @ModelAttribute Review review, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      log.error("Binding result has error! " + bindingResult.getFieldError());
      model.addAttribute("msg", "An error occurred while adding a review");
      return "error/500";
    }

    reviewService.saveReview(review);
    log.info("Save new review to product {} to database", review.getProduct());
    return "redirect:/index";
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/delete/{id}")
  public String deleteComment(@PathVariable("id") Long id) {
    reviewService.deleteReviewById(id);
    log.info("Delete review with id {} from database", id);
    return "redirect:/index";
  }
}
