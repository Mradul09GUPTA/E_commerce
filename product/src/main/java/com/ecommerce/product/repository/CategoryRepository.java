package com.ecommerce.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.product.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    
}
