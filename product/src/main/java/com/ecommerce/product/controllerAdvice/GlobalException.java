package com.ecommerce.product.controllerAdvice;

import com.ecommerce.product.exception.ProductNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<String> handleProductNotFoundExceptionException(ProductNotFound ex) {
        ResponseEntity<String> responseEntity = new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
        return responseEntity;
    }

}
