package com.ecommerce.product.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String title;
    private Double price;
    private String categoryName ;
    private  String categoryDescription;

}
