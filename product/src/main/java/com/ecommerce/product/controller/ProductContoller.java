package com.ecommerce.product.controller;

import com.ecommerce.product.dtos.ProductDto;
import com.ecommerce.product.exception.ProductNotFound;
import com.ecommerce.product.model.Category;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product")
public class ProductContoller {
    @Autowired
    //@Qualifier("DBProductService")
    ProductService productService;


    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) throws ProductNotFound {
        Product product = productService.getProductByID(id);
        ProductDto productDto = from(product);

        ResponseEntity<ProductDto> response = new ResponseEntity<>(productDto,
                HttpStatus.OK
        );


        return response;
    }


    @GetMapping()
    public ResponseEntity<Page<Product>> getAllProduct(@RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        ResponseEntity<Page<Product>> response = new ResponseEntity<>(
                productService.getAllProduct(pageNumber, pageSize),
                HttpStatus.OK
        );


        return response;
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> replaceProduct(@PathVariable("id") Long id, @RequestBody Product product) throws ProductNotFound {

        return new ResponseEntity<>(from(productService.replaceProduct(id, product)), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ProductDto> insertProduct(@RequestBody ProductDto productdto) {
        //TODO: process POST request
        Product product = from(productdto);
        return new ResponseEntity<>(from(productService.insertProduct(product)), HttpStatus.OK);
    }

    private ProductDto from(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setPrice(product.getPrice());
        productDto.setCategoryName(product.getCategory().getName());
        productDto.setCategoryDescription(product.getCategory().getDescription());
        return productDto;
    }

    private Product from(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        Category category = new Category();
        category.setName(productDto.getCategoryName());
        category.setDescription(productDto.getCategoryDescription());
        product.setCategory(category);

        return product;

    }


}
