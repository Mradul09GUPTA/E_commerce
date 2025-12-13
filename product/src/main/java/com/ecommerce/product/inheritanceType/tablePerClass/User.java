package com.ecommerce.product.inheritanceType.tablePerClass;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tpc_users")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private int id;
    private String name;
    private String email;
}
