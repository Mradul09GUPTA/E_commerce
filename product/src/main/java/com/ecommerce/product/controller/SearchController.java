package com.ecommerce.product.controller;


import com.ecommerce.product.dtos.FilterProductDto;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @PostMapping("/{name}")
    public Page<Product> searchProducts(@PathVariable String name, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size, @RequestBody FilterProductDto filterProductDto) {

        return searchService.getProducts(name, page, size, filterProductDto);
    }
}
