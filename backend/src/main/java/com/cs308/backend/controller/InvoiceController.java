package com.cs308.backend.controller;

import com.cs308.backend.dao.Invoice;
import com.cs308.backend.dto.InvoiceResponse;
import com.cs308.backend.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/{invoiceNumber}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable String invoiceNumber) {
        Invoice invoice = invoiceService.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        InvoiceResponse response = new InvoiceResponse(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getOrder().getId(),
                invoice.getPayment().getId(),
                invoice.getInvoiceDate(),
                invoice.getTotalAmount());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{invoiceNumber}/download")
    public ResponseEntity<ByteArrayResource> downloadInvoice(@PathVariable String invoiceNumber) {
        Invoice invoice = invoiceService.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        ByteArrayResource resource = new ByteArrayResource(invoice.getPdfContent());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + invoiceNumber + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(invoice.getPdfContent().length)
                .body(resource);
    }
}