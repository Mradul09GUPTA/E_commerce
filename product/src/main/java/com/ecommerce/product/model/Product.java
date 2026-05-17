package com.ecommerce.product.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product extends Base {
    private String title;
    private Double price;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Category category;


}
