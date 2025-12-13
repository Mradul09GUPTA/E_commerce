package com.ecomerce.payment.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.payment.dto.PaymentLinkReq;
import com.ecomerce.payment.service.PaymentService;
import com.razorpay.RazorpayException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/payment")
public class PaymentContoller {
    @Autowired
    PaymentService payementServices;

    @PostMapping("")
    public String paymentLink(@RequestBody PaymentLinkReq paymentLinkReq) throws RazorpayException {
        //return new String();

        return payementServices.initiatePayment(paymentLinkReq.getOrderID(),paymentLinkReq.getAmount(),paymentLinkReq.getPhoneNumber(),paymentLinkReq.getName());
    }
    @PostMapping("/webhook")
    public void triggerWebhook(){
        System.out.println("Webhook Triggered");
    }
    
}
