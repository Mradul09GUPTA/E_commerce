package com.ecommerce.product.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Filter {
    String name;
    PriceFilter priceFilter;
}
