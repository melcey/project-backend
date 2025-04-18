package com.cs308.backend.service;

import com.cs308.backend.dao.Invoice;
import com.cs308.backend.dao.Payment;
import com.cs308.backend.dao.OrderItem;
import com.cs308.backend.repo.InvoiceRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public Invoice generateInvoice(Payment payment) {
        // Generate unique invoice number
        String invoiceNumber = generateInvoiceNumber();

        // Create invoice
        Invoice invoice = new Invoice(payment.getOrder(), payment, invoiceNumber);

        // Generate PDF
        byte[] pdfContent = generatePDF(invoice);
        invoice.setPdfContent(pdfContent);

        // Save invoice
        invoice = invoiceRepository.save(invoice);

        // Send email with PDF
        sendInvoiceEmail(invoice);

        return invoice;
    }

    private String generateInvoiceNumber() {
        return "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private byte[] generatePDF(Invoice invoice) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Add header
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph header = new Paragraph("INVOICE", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            document.add(new Paragraph(" ")); // Spacing

            // Add invoice details
            document.add(new Paragraph("Invoice Number: " + invoice.getInvoiceNumber()));
            document.add(new Paragraph("Date: " +
                    invoice.getInvoiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(new Paragraph(" ")); // Spacing

            // Add customer details
            document.add(new Paragraph("Bill To:"));
            document.add(new Paragraph(invoice.getOrder().getUser().getName()));
            document.add(new Paragraph(invoice.getOrder().getDeliveryAddress()));
            document.add(new Paragraph(" ")); // Spacing

            // Create table for order items
            PdfPTable table = new PdfPTable(4); // 4 columns
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 2, 1, 1, 1 }); // Relative column widths

            // Add table headers
            Stream.of("Product", "Quantity", "Price", "Total")
                    .forEach(columnTitle -> {
                        PdfPCell header1 = new PdfPCell();
                        header1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header1.setBorderWidth(2);
                        header1.setPhrase(new Phrase(columnTitle));
                        table.addCell(header1);
                    });

            // Add order items
            for (OrderItem item : invoice.getOrder().getOrderItems()) {
                table.addCell(item.getProduct().getName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(item.getPrice().toString());
                table.addCell(item.getPrice().multiply(new BigDecimal(item.getQuantity())).toString());
            }

            document.add(table);
            document.add(new Paragraph(" ")); // Spacing

            // Add total
            Paragraph total = new Paragraph(
                    "Total Amount: $" + invoice.getTotalAmount(),
                    new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private void sendInvoiceEmail(Invoice invoice) {
        String to = invoice.getOrder().getUser().getEmail();
        String subject = "Your Invoice #" + invoice.getInvoiceNumber();
        String text = "Dear " + invoice.getOrder().getUser().getName() + ",\n\n" +
                "Thank you for your purchase. Please find your invoice attached.\n\n" +
                "Best regards,\nYour Store Team";

        emailService.sendInvoiceEmail(to, subject, text, invoice.getPdfContent(), invoice.getInvoiceNumber());
    }
}