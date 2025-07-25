package com.jobportal.JobPortal.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.jobportal.JobPortal.entity.SubscriptionPlan;
import com.jobportal.JobPortal.repository.InvoiceRepository;
import com.jobportal.JobPortal.repository.SubscriptionPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepostory;

    @Autowired
    private SubscriptionPlanRepository subscriptionPlanRepository;

    public byte[] generateInvoice(Long subscriptionId) throws DocumentException {
        Optional<SubscriptionPlan> sub = subscriptionPlanRepository.findById(subscriptionId);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document,outputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.open();
        document.addTitle("Zidio_Connection_Invoice");
        document.add(new Paragraph("INVOICE", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        document.add(new Paragraph("Invoice Number : ZIDIO"+ UUID.randomUUID()));
        document.add(new Paragraph("Invoice Date : "+ LocalDate.now()));
//        document.add(new Paragraph("Email : "+sub.getUse))
        document.close();
        return outputStream.toByteArray();

    }
}
