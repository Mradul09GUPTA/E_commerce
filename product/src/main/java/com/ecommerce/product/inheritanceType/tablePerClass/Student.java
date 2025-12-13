package com.ecommerce.product.inheritanceType.tablePerClass;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="tpc_Student")

public class Student extends User  {
    private Double psp;

}
