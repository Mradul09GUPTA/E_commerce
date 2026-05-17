package com.ecommerce.product.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Product extends Base {
    private String title;
    private Double price;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private Category category;
   @OneToMany(cascade = CascadeType.ALL)
    private List<ProductType> productTypeList;


}
