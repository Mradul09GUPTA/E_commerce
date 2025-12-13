package com.ecomerce.payment.paymentGateway;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class RazorpayPaymentGateway implements PayemntGateway{
    @Autowired
    @Qualifier("getRazorpayClient")
     RazorpayClient instance;


    @Override
    public String generatePaymentLink(Long order_id, Long amount, String phoneNumber,String name) throws RazorpayException {
     
       

        


     JSONObject paymentLinkRequest = new JSONObject();
paymentLinkRequest.put("amount",amount);
paymentLinkRequest.put("currency","INR");
// paymentLinkRequest.put("accept_partial",true);
// paymentLinkRequest.put("first_min_partial_amount",100);
paymentLinkRequest.put("expire_by",System.currentTimeMillis()+10 * 60 * 1000);
paymentLinkRequest.put("reference_id",order_id);
paymentLinkRequest.put("description","Payment for "+order_id);
JSONObject customer = new JSONObject();
customer.put("name",name);
customer.put("contact",phoneNumber);
customer.put("email",name+"@example.com");
paymentLinkRequest.put("customer",customer);
JSONObject notify = new JSONObject();
notify.put("sms",true);
notify.put("email",true);
paymentLinkRequest.put("reminder_enable",true);
JSONObject notes = new JSONObject();
notes.put("policy_name","Jeevan Bima");
paymentLinkRequest.put("notes",notes);
paymentLinkRequest.put("callback_url","https://www.youtube.com/watch?v=6fKphXvCUDg");
paymentLinkRequest.put("callback_method","get");
              
PaymentLink payment = instance.paymentLink.create(paymentLinkRequest);
        
        return payment.get("short_url").toString();
    }
    
}
