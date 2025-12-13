package com.ecomerce.payment.paymentGateway;

import com.razorpay.RazorpayException;

public interface PayemntGateway {
   String generatePaymentLink(Long order_id, Long amount, String phoneNumber,String name) throws RazorpayException;   
}