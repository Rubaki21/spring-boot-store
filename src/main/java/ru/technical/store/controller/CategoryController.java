package ru.technical.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.technical.store.entity.Category;
import ru.technical.store.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
@PreAuthorize("hasAuthority('ADMIN')")
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/new")
  public String createCategory() {
    return "createCategory";
  }

  @PostMapping("/new")
  public String createCategory(@Valid @ModelAttribute Category category, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      log.warn("Binding result has error! {}", bindingResult.getFieldError());
      return "createCategory";
    }

    categoryService.saveCategory(category);
    log.info("Save new category with name {} to database", category.getCategoryName());
    return "redirect:/admin";
  }

  @GetMapping("/edit/{id}")
  public String editCategory(@PathVariable("id") Long id, Model model) {
    Category category = categoryService.getCategoryById(id);
    model.addAttribute("categoryForm", category);
    return "editCategory";
  }

  @PostMapping("/edit/{id}")
  public String editCategory(@Valid @ModelAttribute Category category, BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      log.warn("Binding result has error! {} ", bindingResult.getFieldError());
      model.addAttribute("categoryForm", category);
      return "editCategory";
    }

    categoryService.saveCategory(category);
    log.info("Edit category with id {}", category.getId());
    return "redirect:/admin";
  }

  @PostMapping("/delete/{id}")
  public String deleteCategory(@PathVariable("id") Long id) {
    categoryService.deleteById(id);
    log.info("Delete category with id {} from database", id);
    return "redirect:/admin";
  }
}
