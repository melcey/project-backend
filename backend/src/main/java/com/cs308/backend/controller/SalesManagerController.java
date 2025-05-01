package com.cs308.backend.controller;

import com.cs308.backend.dao.Invoice;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Refund;
import com.cs308.backend.dao.ReturnRequest;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.MessageResponse;
import com.cs308.backend.dto.PricingRequest;
import com.cs308.backend.dto.ReturnRequestResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.InvoiceService;
import com.cs308.backend.service.ProductPricingService;
import com.cs308.backend.service.ReturnRefundService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sales")
public class SalesManagerController {
    
    private final ProductPricingService pricingService;
    private final InvoiceService invoiceService;
    private final ReturnRefundService returnRefundService;
    
    public SalesManagerController(ProductPricingService pricingService,
                                InvoiceService invoiceService,
                                ReturnRefundService returnRefundService) {
        this.pricingService = pricingService;
        this.invoiceService = invoiceService;
        this.returnRefundService = returnRefundService;
    }
    
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }
        
        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
        User user = userDetails.getUser();
        
        if (user.getRole() != Role.sales_manager) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a sales manager");
        }
        
        return user;
    }
    
    @GetMapping("/unpriced-products")
    public ResponseEntity<?> getUnpricedProducts() {
        getCurrentUser();
        
        List<Product> unpricedProducts = pricingService.getAllUnpricedProducts();
        return ResponseEntity.ok(unpricedProducts);
    }
    
    @PutMapping("/approve-price")
    public ResponseEntity<?> approveProductPrice(@RequestBody PricingRequest request) {
        getCurrentUser();
        
        Product product = pricingService.approveProductPrice(
            request.getProductId(), 
            request.getPrice(), 
            request.getCostPrice()
        );
        
        return ResponseEntity.ok(product);
    }
    
    @PutMapping("/apply-discount")
    public ResponseEntity<?> applyDiscount(@RequestBody Map<String, Object> request) {
        getCurrentUser();
        
        @SuppressWarnings("unchecked")
        List<Long> productIds = (List<Long>) request.get("productIds");
        BigDecimal discountRate = new BigDecimal(request.get("discountRate").toString());
        
        List<Product> updatedProducts = pricingService.applyDiscountToProducts(productIds, discountRate);
        return ResponseEntity.ok(updatedProducts);
    }
    
    @PutMapping("/remove-discount")
    public ResponseEntity<?> removeDiscount(@RequestBody Map<String, Object> request) {
        getCurrentUser();
        
        @SuppressWarnings("unchecked")
        List<Long> productIds = (List<Long>) request.get("productIds");
        
        List<Product> updatedProducts = pricingService.removeDiscountsFromProducts(productIds);
        return ResponseEntity.ok(updatedProducts);
    }
    
    @GetMapping("/invoices")
    public ResponseEntity<?> getInvoicesInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        getCurrentUser();
        
        List<Invoice> invoices = pricingService.getInvoicesInDateRange(startDate, endDate);
        return ResponseEntity.ok(invoices);
    }
    
    @GetMapping("/invoices/{invoiceNumber}/download")
    public ResponseEntity<ByteArrayResource> downloadInvoice(@PathVariable String invoiceNumber) {
        getCurrentUser();
        
        Invoice invoice = invoiceService.findByInvoiceNumber(invoiceNumber)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));
            
        ByteArrayResource resource = new ByteArrayResource(invoice.getPdfContent());
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + invoiceNumber + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(invoice.getPdfContent().length)
            .body(resource);
    }
    
    @GetMapping("/revenue-profit")
    public ResponseEntity<?> getRevenueAndProfit(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        getCurrentUser();
        
        Map<String, Object> results = pricingService.calculateRevenueAndProfit(startDate, endDate);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/revenue-chart")
    public ResponseEntity<?> getRevenueChart(@RequestParam int year) {
        getCurrentUser();
        
        List<Map<String, Object>> chartData = pricingService.generateMonthlyRevenueChart(year);
        return ResponseEntity.ok(chartData);
    }
    
    @GetMapping("/return-requests")
    public ResponseEntity<?> getPendingReturnRequests() {
        getCurrentUser();
        
        List<ReturnRequest> requests = returnRefundService.getPendingReturnRequests();
        List<ReturnRequestResponse> response = requests.stream()
            .map(ReturnRequestResponse::new)
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/return-requests/{id}/process")
    public ResponseEntity<?> processReturnRequest(
            @PathVariable Long id,
            @RequestParam boolean approve) {
        User salesManager = getCurrentUser();
        
        Refund refund = returnRefundService.processReturnRequest(id, salesManager, approve);
        
        if (approve && refund != null) {
            return ResponseEntity.ok(refund);
        } else {
            return ResponseEntity.ok(new MessageResponse("Return request has been rejected"));
        }
    }
    
    @PostMapping("/refunds/{id}/complete")
    public ResponseEntity<?> completeRefund(@PathVariable Long id) {
        getCurrentUser();
        
        Refund completedRefund = returnRefundService.completeRefund(id);
        return ResponseEntity.ok(completedRefund);
    }
}