package com.ecommerce.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FakeProductDto {
    Long id;
    String title;
    Double price;
    String description;
    String category;
    String image;
}
