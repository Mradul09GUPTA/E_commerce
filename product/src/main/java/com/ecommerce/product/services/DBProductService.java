package com.ecommerce.product.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce.product.exception.ProductNotFound;
import com.ecommerce.product.model.Product;
import com.ecommerce.product.projection.ProductWithTitleAndId;
import com.ecommerce.product.repository.ProductRepository;
@Service("DBProductService")
public class DBProductService implements ProductService  {
    @Autowired
    ProductRepository productRepository;

    @Override
    public Product getProductByID(Long id) throws ProductNotFound {
        // TODO Auto-generated method stub
        //Long id = (Long)id;
         Product product = productRepository.findById(id).orElseThrow(()->new ProductNotFound("Product is "+ id + "not found"));
         return product;

        // throw new UnsupportedOperationException("Unimplemented method 'getProductByID'");
    }

    @Override
    public Page<Product> getAllProduct(int pageNumber,int pageSize) {
        // TODO Auto-generated method stub
       
       
       List<ProductWithTitleAndId> productWithTitleAndIds = productRepository.randomSearchMethodForProduct();
       for(ProductWithTitleAndId productWithTitleAndId :productWithTitleAndIds){
        System.out.println("id "+productWithTitleAndId.getId() +" and title " +productWithTitleAndId.getTitle());
       }
       
       List<Object[]> rows = productRepository.debugProducts();
     for (Object[] r : rows) {
    System.out.println(r[0] + " | " + r[1]); // should show actual DB values
        }
       Product productoneid = productRepository.findonlyoneproduct((long) 1).get();
       System.out.println(productoneid.getTitle());
      
       //page
      

      Sort sort = Sort.by("price").ascending().and(Sort.by("title")).descending();

       Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

      Page<Product> products = productRepository.findAll(pageable);




       return products;
       
       
        // throw new UnsupportedOperationException("Unimplemented method 'getAllProduct'");

    }

    @Override
    public Product replaceProduct(Long id, Product product) throws ProductNotFound {
        // TODO Auto-generated method stub


         Product replaceproduct = productRepository.findById(id).orElseThrow(()->new ProductNotFound("Product is "+ id + "not found"));
        replaceproduct.setCategory(product.getCategory());
        replaceproduct.setPrice(product.getPrice());
        replaceproduct.setTitle(product.getTitle());
         
        
        
         return productRepository.save(replaceproduct);
        //throw new UnsupportedOperationException("Unimplemented method 'replaceProduct'");
    }

    @Override
    public Product insertProduct(Product product) {
        

        return  productRepository.save(product);
        //throw new UnsupportedOperationException("Unimplemented method 'insertProduct'");
    }
    
}
