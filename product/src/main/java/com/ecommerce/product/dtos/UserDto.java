package com.ecommerce.product.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String email;
    private List<String> role;
    private String Status;
}
