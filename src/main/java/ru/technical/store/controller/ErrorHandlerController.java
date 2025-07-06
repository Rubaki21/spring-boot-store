package ru.technical.store.controller;

import ru.technical.store.exception.CategoryIsNotEmptyException;
import ru.technical.store.exception.ProductNotFoundException;
import ru.technical.store.exception.UserAlreadyExistsException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlerController {

  @ExceptionHandler(ProductNotFoundException.class)
  // Обработчик ProductNotFoundException. Помещает аттрибут с сообщением для пользователя на 404.html
  public String handleProductNotFoundException(Model model) {
    model.addAttribute("msg", "Данный товар не найден");
    return "error/404";
  }

  @ExceptionHandler(CategoryIsNotEmptyException.class)
  // Обработчик CategoryIsNotEmptyException. Помещает аттрибут с сообщением для пользователя на 500.html
  public String handleCategoryIsNotEmptyException(Model model) {
    model.addAttribute("msg", "Данная категория содержит в себе товары и не может быть удалена");
    return "error/500";
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  // Обработчик UserAlreadyExistsException. Помещает аттрибут с сообщением для пользователя на 500.html
  public String handleUserAlreadyExistsException(Model model) {
    model.addAttribute("msg", "Пользователь с данным email уже существует");
    return "error/500";
  }
}