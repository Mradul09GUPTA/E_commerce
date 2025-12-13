package com.ecommerce.product.controllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ecommerce.product.exception.ProductNotFound;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(ProductNotFound.class)
     public ResponseEntity<String> handleProductNotFoundExceptionException(ProductNotFound ex) {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(
                 ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return responseEntity;
    }
    
}
