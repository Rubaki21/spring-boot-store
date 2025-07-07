package ru.technical.store.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

  private String title;
  private String imageLocation;
  private String categoryName;
  private String price;
  private String quantity;
}
