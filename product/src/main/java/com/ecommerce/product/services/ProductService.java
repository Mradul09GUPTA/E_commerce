package com.ecommerce.product.services;

import java.util.List;

import com.ecommerce.product.dto.FakeProductDto;
import com.ecommerce.product.exception.ProductNotFound;
import com.ecommerce.product.model.Product;

public interface ProductService {
    Product getProductByID(Long id) throws ProductNotFound;

    List<Product> getAllProduct();

    Product replaceProduct(Long id, Product product) throws ProductNotFound;

    Product insertProduct(Product product);

}
