package com.ecomerce.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentLinkReq {
    Long orderID;
    Long amount;
    String phoneNumber;
    String name;

    
}
