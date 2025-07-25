package com.jobportal.JobPortal.controller;

import com.jobportal.JobPortal.Enum.PaymentType;
import com.jobportal.JobPortal.entity.Invoice;
import com.jobportal.JobPortal.entity.Recruiter;
import com.jobportal.JobPortal.entity.SubscriptionPlan;
import com.jobportal.JobPortal.repository.InvoiceRepository;
import com.jobportal.JobPortal.repository.RecruiterRepository;
import com.jobportal.JobPortal.repository.SubscriptionPlanRepository;
import com.jobportal.JobPortal.service.InvoiceService;
import com.jobportal.JobPortal.service.RazorPayService;

import com.jobportal.JobPortal.service.RecruiterService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/razorpay")
public class RazorPayController {
    @Autowired
    private RazorPayService razorPayService;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private InvoiceService invoiceService;



    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository; // You will need to inject this

    @Autowired
    private InvoiceRepository invoiceRepository; // You will need to inject this

    @PostMapping("payment/webhook")
    public ResponseEntity<String> paymentWebhook(@RequestBody String payload, @RequestHeader("X-RazorPay-Signature")String signature) {
        String secret="YOUR_WEBHOOK_SECRET";
        try {
            // Verify the signature
            Utils.verifyWebhookSignature(payload,signature,secret);

            // Extract the payment details from the payload
            JSONObject json = new JSONObject(payload);
            JSONObject event = json.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
            String razorpayOrderId = event.getString("order_id");
            String razorpayPaymentId = event.getString("id");
            String status = event.getString("status");

            // Find the pending records and update them
            if ("captured".equals(status)) {
                // Find the subscription plan using the Razorpay order ID
                SubscriptionPlan subscription = subscriptionPlanRepository.findByRazorpayOrderId(razorpayOrderId);
                if (subscription != null) {
                    subscription.setStatus("PAID");
                    subscription.setRazorpayPaymentId(razorpayPaymentId);
                    subscriptionPlanRepository.save(subscription);

                    // Find the corresponding invoice and update it
                    Invoice invoice = invoiceRepository.findBySubscriptionPlanId(subscription.getId());
                    if (invoice != null) {
                        invoice.setStatus("PAID");
                        invoice.setPaymentMethod(PaymentType.RAZORPAY);
                        invoiceRepository.save(invoice);

                        // You can now generate the PDF invoice and store the download URL
                        byte[] pdf = invoiceService.generateInvoice(subscription.getId());
                    }
                }
            }
            return ResponseEntity.ok("success");
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid signature or processing failed");
        }
    }

    @PostMapping("create-order")
    public ResponseEntity<String> createOrder(@RequestParam Double amount,@RequestParam String currency,@RequestParam String receipt) {
        try{
            String orderJson=razorPayService.createOrder1(amount,currency,receipt);
            return ResponseEntity.ok(orderJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed to create order");
        }
    }
    @PostMapping("/create-order/{recruiterId}")
    public ResponseEntity<?> createRazorpayOrder(@PathVariable Long recruiterId, @RequestBody Double amount) {
        try {
            Recruiter recruiter = recruiterRepository.findById(recruiterId)
                    .orElseThrow(() -> new RuntimeException("Recruiter not found"));

            Order order = razorPayService.createOrder(recruiter, amount);
            return ResponseEntity.ok(order.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating Razorpay order: " + e.getMessage());
        }
    }

}
