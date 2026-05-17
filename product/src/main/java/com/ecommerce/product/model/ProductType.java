package com.ecommerce.product.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ProductType extends  Base {
    private String type;
}
// Adult
// Children
// Private
// Public
