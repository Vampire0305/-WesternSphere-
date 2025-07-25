package com.jobportal.JobPortal.controller;

import com.jobportal.JobPortal.DTO.InvoiceDTO;
import com.jobportal.JobPortal.entity.Invoice;
import com.jobportal.JobPortal.entity.Recruiter;
import com.jobportal.JobPortal.entity.SubscriptionPlan;
import com.jobportal.JobPortal.repository.RecruiterRepository;
import com.jobportal.JobPortal.security.InvoiceGenerator;
import com.jobportal.JobPortal.service.BillingService;
import com.jobportal.JobPortal.service.RazorPayService;
import com.jobportal.JobPortal.service.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoice")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @Autowired
    private RazorPayService razorPayService;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private InvoiceGenerator invoiceGenerator;

    @PostMapping("/pay")
    public ResponseEntity<Invoice> pay(@RequestBody InvoiceDTO dto) {
        return ResponseEntity.ok(billingService.createInvoice(dto));
    }
    @GetMapping("/history/{email}")
    public ResponseEntity<List<Invoice>> history(@PathVariable String email) {
        return ResponseEntity.ok(billingService.getAllInvoices(email));
    }
    @PostMapping("createordr")
    public ResponseEntity<String> createOrder(@RequestBody Double amount) {
        String order= razorPayService.createOrder1(amount,"INR","rcpt-"+System.currentTimeMillis());
        return ResponseEntity.ok(order);
    }
    @PostMapping("create-order")
    public ResponseEntity<String> createOrder1(@RequestBody Map<String, Object> requestBody) {
        Long recruiterId = Long.valueOf(requestBody.get("recruiterId").toString());
        Double amount = Double.valueOf(requestBody.get("amount").toString());
        String currency = "INR"; // You can make this configurable
        String receipt = "rcpt-" + System.currentTimeMillis();

        try {
            String orderJson = billingService.createOrderAndInvoice(recruiterId, amount, currency, receipt);
            return ResponseEntity.ok(orderJson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    /**
     * This endpoint is called by Razorpay.
     * WARNING: Signature verification is disabled for Postman testing.
     * DO NOT USE IN PRODUCTION.
     */
    @PostMapping("/razorpay/webhook")
    public ResponseEntity<ByteArrayInputStream> paymentWebhook(
            @RequestBody String payload,
            @RequestHeader(value = "X-RazorPay-Signature", required = false) String signature) {

        try {
            // NOTE: The signature verification is TEMPORARILY DISABLED for Postman testing.
            // In a production environment, this check is crucial for security.
            // Uncomment the following line for a real implementation:
            // String secret = "YOUR_WEBHOOK_SECRET";
            // Utils.verifyWebhookSignature(payload, signature, secret);

            // Delegate to the service to handle the payment logic
            ByteArrayInputStream pdf=billingService.handleSuccessfulPayment(payload);
            return ResponseEntity.ok(pdf);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
    @GetMapping("/download/{recId}")
    public ResponseEntity<ByteArrayInputStream> download(@PathVariable Long recId) {
        Recruiter recruiter=recruiterRepository.findById(recId).orElse(null);
        SubscriptionPlan sub= (recruiter!=null) ? recruiter.getSubscriptionPlan() : null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Invoice.pdf");

        return new ResponseEntity<>(InvoiceGenerator.generateInvoice(recId,sub), headers, HttpStatus.OK);


    }
}

