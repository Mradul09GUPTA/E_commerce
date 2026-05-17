package com.ecommerce.product.services;

import com.ecommerce.product.dtos.Filter;
import com.ecommerce.product.dtos.FilterProductDto;
import com.ecommerce.product.dtos.PriceFilter;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getProducts(
            String name,
            int pageNumber,
            int size,
            FilterProductDto filterProductDto
    ) {

        Sort sort = Sort.by("id").ascending();

        if (!filterProductDto.getFilters().isEmpty()) {

            for (Filter filter : filterProductDto.getFilters()) {

                if (filter.getPriceFilter() == PriceFilter.HightToLow) {

                    sort = Sort.by(filter.getName()).descending()
                            .and(Sort.by("id").ascending());

                } else if (filter.getPriceFilter() == PriceFilter.LowToHigh) {

                    sort = Sort.by(filter.getName()).ascending()
                            .and(Sort.by("id").ascending());
                }
            }
        }

        Pageable page = PageRequest.of(pageNumber, size, sort);

        return productRepository
                .findByTitleContainingIgnoreCase(name, page);
    }
}