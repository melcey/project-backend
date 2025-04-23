package com.cs308.backend.controller;

import com.cs308.backend.dao.Invoice;
import com.cs308.backend.dao.OrderItem;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.InvoiceResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.InvoiceService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/{invoiceNumber}")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable String invoiceNumber) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }
        else if (user.getRole() != Role.product_manager) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Invoice> retrievedInvoice = invoiceService.findByInvoiceNumber(invoiceNumber);

        if (!(retrievedInvoice.isPresent())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice could not be found");
        }

        Invoice invoice = retrievedInvoice.get();

        if ((user.getRole().equals(Role.customer)) && (!(invoice.getPayment().getOrder().getUser().equals(user)))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invoice does not belong to the user");
        }
        else if (user.getRole().equals(Role.product_manager)) {
                int managedCount = 0;

                for (OrderItem orderedItem: invoice.getPayment().getOrder().getOrderItems()) {
                        if (orderedItem.getProduct().getProductManager().equals(user)) {
                                managedCount += 1;
                        }
                }

                if (managedCount == 0) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No products in the associated order belong to the product manager");
                }
        }

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
            
        User user = userDetails.getUser();

        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }
        else if (user.getRole() != Role.product_manager) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized");
        }

        Optional<Invoice> retrievedInvoice = invoiceService.findByInvoiceNumber(invoiceNumber);

        if (!(retrievedInvoice.isPresent())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice could not be found");
        }

        Invoice invoice = retrievedInvoice.get();

        if ((user.getRole().equals(Role.customer)) && (!(invoice.getPayment().getOrder().getUser().equals(user)))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invoice does not belong to the user");
        }
        else if (user.getRole().equals(Role.product_manager)) {
                int managedCount = 0;

                for (OrderItem orderedItem: invoice.getPayment().getOrder().getOrderItems()) {
                        if (orderedItem.getProduct().getProductManager().equals(user)) {
                                managedCount += 1;
                        }
                }

                if (managedCount == 0) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No products in the associated order belong to the product manager");
                }
        }

        ByteArrayResource resource = new ByteArrayResource(invoice.getPdfContent());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + invoiceNumber + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(invoice.getPdfContent().length)
                .body(resource);
    }
}