package com.ecommerce.product.inheritanceType.joined;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="joinesStudent")
@PrimaryKeyJoinColumn(name="user_id")
public class Student extends User {
    Double psp;
}
