package com.jobportal.JobPortal.service;

import com.jobportal.JobPortal.entity.Recruiter;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorPayService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    String keySecret="YOUR_WEBHOOK_SECRET";

    public Order createOrder(Recruiter recruiter, double amount) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(key, secret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Amount in paisa
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "receipt_for_recruiter_" + recruiter.getId()); // Changed to recruiter ID
        orderRequest.put("payment_capture", 1);

        Order order = razorpay.orders.create(orderRequest);
        return order;
    }

    public String createOrder1(Double amount , String currency,String receipt) {
        try {
            RazorpayClient client = new RazorpayClient(key, secret);
            JSONObject obj = new JSONObject();
            obj.put("amount", amount);
            obj.put("currency", currency);
            obj.put("receipt", receipt);
            Order order = client.orders.create(obj);
            return order.toString();

        } catch (Exception e) {
            throw new RuntimeException("error creating razorpay order"+e.getMessage(), e);
        }
    }
    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);
            return Utils.verifyPaymentSignature(options, keySecret);
        } catch (RazorpayException e) {
            System.err.println("Razorpay Signature Verification Failed: " + e.getMessage());
            return false;
        }
}
}
