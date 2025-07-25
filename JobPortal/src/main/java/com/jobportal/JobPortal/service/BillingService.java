package com.jobportal.JobPortal.service;

import com.jobportal.JobPortal.DTO.InvoiceDTO;
import com.jobportal.JobPortal.Enum.PaymentType;
import com.jobportal.JobPortal.entity.Invoice;
import com.jobportal.JobPortal.entity.Recruiter;
import com.jobportal.JobPortal.entity.SubscriptionPlan;
import com.jobportal.JobPortal.repository.InvoiceRepository;
import com.jobportal.JobPortal.repository.RecruiterRepository;
import com.jobportal.JobPortal.repository.SubscriptionPlanRepository;
import com.jobportal.JobPortal.security.InvoiceGenerator;
import org.cloudinary.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillingService {

    @Autowired
    private InvoiceRepository invoiceRepostory;

    public Invoice createInvoice(InvoiceDTO invoiceDTO) {
        Invoice invoice = new Invoice();
        invoice.setId(invoiceDTO.getId());
        invoice.setUserEmail(invoiceDTO.getUserEmail());
        invoice.setServiceType(invoiceDTO.getServiceType());
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setInvoiceDownloadUrl(invoiceDTO.getInvoiceDownloadUrl());
        invoice.setAmount(invoiceDTO.getAmount());
        invoice.setStatus(invoiceDTO.getStatus());
        invoice.setPaymentMethod(invoiceDTO.getPaymentMethod());
        return invoiceRepostory.save(invoice);
    }

    public List<Invoice> getAllInvoices(String userEmail) {
        return invoiceRepostory.findByUserEmail(userEmail);
    }
    @Autowired
    private InvoiceRepository invoiceRepository; // Corrected to InvoiceRepository

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository; // Inject SubscriptionPlanRepository

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private RazorPayService razorPayService;

    @Autowired
    private InvoiceGenerator invoiceGenerator;

    // This method handles both order creation and initial pending record creation
    public String createOrderAndInvoice(Long recruiterId, Double amount, String currency, String receipt) {
        System.out.println(1111111111);

        // Create the Razorpay order
        String orderJson = razorPayService.createOrder1(amount, currency, receipt);
        System.out.println(222222222);

        // Parse the orderId from the JSON response
        String razorpayOrderId = new JSONObject(orderJson).getString("id");
        Recruiter recruiter = recruiterRepository.findById(recruiterId).orElse(null);
        System.out.println(33333);


        // 1. Create a pending SubscriptionPlan
        SubscriptionPlan subscription = new SubscriptionPlan();
        // You'll need to set other properties like name, duration, etc. here
        subscription.setName(recruiter.getName());
        subscription.setEmail(recruiter.getEmail());
        subscription.setDescription("Simple payment");
        subscription.setDurationInDays(365);
        subscription.setPrice(amount);
        subscription.setRazorpayOrderId(razorpayOrderId);
        subscription.setStatus("PENDING"); // Set status to PENDING
        subscriptionPlanRepository.save(subscription);

        // 2. Create a pending Invoice linked to the subscription
        Invoice invoice = new Invoice();
        invoice.setUserEmail(recruiter!=null ?recruiter.getEmail(): null); // Get this from RecruiterRepository
        invoice.setAmount(amount);
        invoice.setServiceType("JobPostingPlan");
        invoice.setStatus("PENDING"); // Set status to PENDING
        invoice.setInvoiceNumber("INV-" + System.currentTimeMillis());
        invoice.setSubscriptionPlanId(subscription.getId());
        invoiceRepository.save(invoice);
        System.out.println(4444);

        return orderJson;
    }
    @Autowired
    private InvoiceService invoiceService;
    // Step 2: Handle the successful payment from the webhook
    public ByteArrayInputStream handleSuccessfulPayment(String payload) throws Exception {
        JSONObject event = new JSONObject(payload).getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
        String razorpayOrderId = event.getString("order_id");
        String razorpayPaymentId = event.getString("id");
        String status = event.getString("status");

        if ("captured".equals(status)) {
            SubscriptionPlan subscription = subscriptionPlanRepository.findByRazorpayOrderId(razorpayOrderId);
            if (subscription != null) {
                subscription.setStatus("PAID");
                subscription.setRazorpayPaymentId(razorpayPaymentId);
                subscription.setStartAt(LocalDateTime.now());
                subscription.setEndAt(LocalDateTime.now().plusDays(365));
                Recruiter recruiter=recruiterRepository.findByEmail(subscription.getEmail()).orElse(null);

                subscriptionPlanRepository.save(subscription);

                Invoice invoice = invoiceRepository.findBySubscriptionPlanId(subscription.getId());
                if (invoice != null) {
                    invoice.setStatus("PAID");
                    invoice.setPaymentMethod(PaymentType.RAZORPAY);
                    invoiceRepository.save(invoice);

                    // Generate the PDF invoice here
                    ByteArrayInputStream pdfBytes = invoiceGenerator.generateInvoice(recruiter.getId(),subscription);
                    // You would save this PDF to a file system or cloud storage and
                    // store the URL in the database.
                    String downloadUrl = "http://your-app/api/invoice/download/" + invoice.getSubscriptionPlanId();
                    invoice.setInvoiceDownloadUrl(downloadUrl);
                    invoiceRepository.save(invoice);
                    return pdfBytes;
                }
            }
        }
        return null;
    }

}
