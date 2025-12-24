package com.ecommerce.product.model;

import java.util.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@MappedSuperclass
public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Date createdAt;
    Date updatedAt;
    
}
