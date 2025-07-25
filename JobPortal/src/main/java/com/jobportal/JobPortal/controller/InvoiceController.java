package com.jobportal.JobPortal.controller;

import com.jobportal.JobPortal.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoice-download")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // This endpoint should be secured to only allow the recruiter to download their own invoice
    @GetMapping("/download/{subscriptionPlanId}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long subscriptionPlanId) throws Exception {
        byte[] pdfBytes = invoiceService.generateInvoice(subscriptionPlanId); // This method needs to be in InvoiceService

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Invoice.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
