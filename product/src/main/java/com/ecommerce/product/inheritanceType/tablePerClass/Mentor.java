package com.ecommerce.product.inheritanceType.tablePerClass;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="tpc_Mentor")
public class Mentor extends User {
    private String company;
}
