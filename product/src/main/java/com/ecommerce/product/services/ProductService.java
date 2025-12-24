package com.ecommerce.product.services;


import org.springframework.data.domain.Page;

import com.ecommerce.product.exception.ProductNotFound;
import com.ecommerce.product.model.Product;

public interface ProductService {
    Product getProductByID(Long id) throws ProductNotFound;

    Page<Product> getAllProduct(int pageNumber,int pageSize);

    Product replaceProduct(Long id, Product product) throws ProductNotFound;

    Product insertProduct(Product product);

}
