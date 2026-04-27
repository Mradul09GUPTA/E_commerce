package com.ecommerce.product.controller;

import com.ecommerce.product.dtos.ProductDto;
import com.ecommerce.product.exception.ProductNotFound;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductContoller.class)
class ProductContollerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    private Product getSampleProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setTitle("title");
        product.setPrice(2.0);
        Category category = new Category();
        category.setId(1L);
        category.setName("category");
        product.setCategory(category);
        return product;
    }

    @Test
    void getproductById_ValidProductId_ReturnProduct() throws Exception {
        //Get: /product/id
        //Arrage
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setTitle("title");
        productDto.setPrice(2.0);
        productDto.setCategoryName("category");

        Product product = getSampleProduct();


        when(productService.getProductByID(1L)).thenReturn(product);
        String expectedResponse = objectMapper.writeValueAsString(productDto);

        mockMvc.perform(get("/product/1"))
                .andExpectAll(status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(expectedResponse)
                );


    }

    @Test
    void getproductById_InvalidProductId_throwException() throws Exception {
        when(productService.getProductByID(1L)).thenThrow(new ProductNotFound("Product not found"));

        mockMvc.perform(get("/product/1"))
                .andExpectAll(status().isNotFound(),
                        content().string("Product not found"));

    }

    @Test
    void postproduct_ValidProductId_ReturnProduct() throws Exception {
        //arrage
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setTitle("title");
        productDto.setPrice(2.0);
        productDto.setCategoryName("category");

        Product product = getSampleProduct();
        when(productService.insertProduct(any(Product.class))).thenReturn(product);
        //post:/product
        mockMvc.perform(post("/product") //Act
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpectAll( // assert
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().string(objectMapper.writeValueAsString(productDto))
                );


    }

}