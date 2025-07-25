package com.jobportal.JobPortal.security;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jobportal.JobPortal.entity.PaidSubscription;
import com.jobportal.JobPortal.entity.SubscriptionPlan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;

@Service
public class InvoiceGenerator {

    public static ByteArrayInputStream generateInvoice(Long recruiterId,SubscriptionPlan sub) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            Font titlefont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
            Paragraph title=new Paragraph("Subscription Invoice ", titlefont);
            document.add(title);
            document.add(new Paragraph(" "));
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(80);
            table.setSpacingBefore(20);
            table.addCell("Subscription ID");
            table.addCell(sub.getId().toString());

            table.addCell("Recruiter ID" );
            table.addCell(recruiterId.toString());

            table.addCell("Amount" );
            table.addCell(sub.getPrice().toString());

            table.addCell("Currency" );
            table.addCell("INR");

            table.addCell("Payment Status" );
            table.addCell(sub.getStatus());

            table.addCell("Razorpay Order ID" );
            table.addCell(sub.getRazorpayOrderId());

            table.addCell("Razorpay Payment ID" );
            table.addCell(sub.getRazorpayPaymentId());


            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            table.addCell("Start Date" );
            table.addCell(sub.getStartAt().format(fmt));

            table.addCell("End Date" );
            table.addCell(sub.getEndAt().format(fmt));
            document.add(table);
            document.close();





        } catch (Exception e) {
            e.printStackTrace();
        }
            return new ByteArrayInputStream(baos.toByteArray());
    }
}
