package com.ecommerce.product.repository;

import com.ecommerce.product.model.Product;
import com.ecommerce.product.projection.ProductWithTitleAndId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query("select p.id as id, p.title as title from Product p")
    List<ProductWithTitleAndId> randomSearchMethodForProduct();

    @Query("select p.id, p.title from Product p")
    List<Object[]> debugProducts();

    @Query("select p  from Product p where p.id=:id")
    Optional<Product> findonlyoneproduct(@Param("id") Long id);

    Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);


}
