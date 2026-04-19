package com.ecommerce.product.model;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Category extends Base {
    private String name;
    private String description;
     @OneToMany(mappedBy = "category",fetch = FetchType.LAZY)
     private List<Product> product;


    /*
     category      product
       1             M
       1             1

       1:m


    */
}
