package com.ecommerce.product.services;

import com.ecommerce.product.exception.ProductNotFound;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;
    @MockBean
    ProductRepository productRepository;

    @Test
    void getProductByID_withPositiveProductId_returnProduct() throws ProductNotFound {
        //Arrage
        Product product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setPrice(2.00);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));


        //ACT
        Product product1 = productService.getProductByID(1L);

        //Assert
        assertNotNull(product1);
        assertEquals(product, product1);


    }

    @Test
    void getProductByID_withNegativeProductID_returnException() {

        //act and assert
        assertThrows(ProductNotFound.class, () -> productService.getProductByID(-1L));

    }

    @Test
    void GetProductByID_ProductNotFound_notfoundInDB() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ProductNotFound.class, () -> {
            productService.getProductByID(1L);
        });


        assertEquals("Product is 1 not found", exception.getMessage());

    }


}