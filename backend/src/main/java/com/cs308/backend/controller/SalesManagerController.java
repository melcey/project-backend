package com.cs308.backend.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.cs308.backend.dao.Invoice;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Refund;
import com.cs308.backend.dao.ReturnRequest;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.CategoryResponse;
import com.cs308.backend.dto.InvoiceResponse;
import com.cs308.backend.dto.MessageResponse;
import com.cs308.backend.dto.PricingRequest;
import com.cs308.backend.dto.ProductResponse;
import com.cs308.backend.dto.RefundResponse;
import com.cs308.backend.dto.ReturnRequestResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.InvoiceService;
import com.cs308.backend.service.ProductPricingService;
import com.cs308.backend.service.ReturnRefundService;

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
        unpricedProducts.sort(Comparator.comparing(Product::getId));

        List<ProductResponse> responseUnpricedProducts = new ArrayList<>();

        for (Product unpricedProduct: unpricedProducts) {
            responseUnpricedProducts.add(new ProductResponse(unpricedProduct.getId(), unpricedProduct.getName(), unpricedProduct.getModel(), unpricedProduct.getSerialNumber(), unpricedProduct.getDescription(), unpricedProduct.getQuantityInStock(), unpricedProduct.getPrice(), unpricedProduct.getWarrantyStatus(), unpricedProduct.getDistributorInfo(), unpricedProduct.getIsActive(), unpricedProduct.getImageUrl(), new CategoryResponse(unpricedProduct.getCategory().getId(), unpricedProduct.getCategory().getName(), unpricedProduct.getCategory().getDescription())));
        }

        return ResponseEntity.ok(responseUnpricedProducts);
    }
    
    @PutMapping("/approve-price")
    public ResponseEntity<?> approveProductPrice(@RequestBody PricingRequest request) {
        getCurrentUser();
        
        Product product = pricingService.approveProductPrice(
            request.getProductId(), 
            request.getPrice(), 
            request.getCostPrice()
        );

        ProductResponse responseProduct = new ProductResponse(product.getId(), product.getName(), product.getModel(), product.getSerialNumber(), product.getDescription(), product.getQuantityInStock(), product.getPrice(), product.getWarrantyStatus(), product.getDistributorInfo(), product.getIsActive(), product.getImageUrl(), new CategoryResponse(product.getCategory().getId(), product.getCategory().getName(), product.getCategory().getDescription()));
        
        return ResponseEntity.ok(responseProduct);
    }
    
    @PutMapping("/apply-discount")
    public ResponseEntity<?> applyDiscount(@RequestBody Map<String, Object> request) {
        getCurrentUser();
        
        @SuppressWarnings("unchecked")
        List<Long> productIds = (List<Long>) request.get("productIds");
        BigDecimal discountRate = new BigDecimal(request.get("discountRate").toString());
        
        List<Product> updatedProducts = pricingService.applyDiscountToProducts(productIds, discountRate);
        updatedProducts.sort(Comparator.comparing(Product::getId));

        List<ProductResponse> responseUpdatedProducts = new ArrayList<>();

        for (Product updatedProduct: updatedProducts) {
            responseUpdatedProducts.add(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
        }

        return ResponseEntity.ok(responseUpdatedProducts);
    }
    
    @PutMapping("/remove-discount")
    public ResponseEntity<?> removeDiscount(@RequestBody Map<String, Object> request) {
        getCurrentUser();
        
        @SuppressWarnings("unchecked")
        List<Long> productIds = (List<Long>) request.get("productIds");
        
        List<Product> updatedProducts = pricingService.removeDiscountsFromProducts(productIds);
        updatedProducts.sort(Comparator.comparing(Product::getId));
        
        List<ProductResponse> responseUpdatedProducts = new ArrayList<>();

        for (Product updatedProduct: updatedProducts) {
            responseUpdatedProducts.add(new ProductResponse(updatedProduct.getId(), updatedProduct.getName(), updatedProduct.getModel(), updatedProduct.getSerialNumber(), updatedProduct.getDescription(), updatedProduct.getQuantityInStock(), updatedProduct.getPrice(), updatedProduct.getWarrantyStatus(), updatedProduct.getDistributorInfo(), updatedProduct.getIsActive(), updatedProduct.getImageUrl(), new CategoryResponse(updatedProduct.getCategory().getId(), updatedProduct.getCategory().getName(), updatedProduct.getCategory().getDescription())));
        }

        return ResponseEntity.ok(responseUpdatedProducts);
    }
    
    @GetMapping("/invoices")
    public ResponseEntity<?> getInvoicesInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        getCurrentUser();
        
        List<Invoice> invoices = pricingService.getInvoicesInDateRange(startDate, endDate);
        invoices.sort(Comparator.comparing(Invoice::getInvoiceDate).reversed());
        
        List<InvoiceResponse> responseInvoices = new ArrayList<>();

        for (Invoice invoice: invoices) {
            responseInvoices.add(new InvoiceResponse(invoice.getId(), invoice.getInvoiceNumber(), invoice.getOrder().getId(), invoice.getPayment().getId(), invoice.getInvoiceDate(), invoice.getTotalAmount()));
        }

        return ResponseEntity.ok(responseInvoices);
    }
    
    @GetMapping("/invoices/{invoiceNumber}/download")
    public ResponseEntity<ByteArrayResource> downloadInvoice(@PathVariable String invoiceNumber) {
        getCurrentUser();
        
        Invoice invoice = invoiceService.findByInvoiceNumber(invoiceNumber)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invoice not found"));
            
        ByteArrayResource resource = new ByteArrayResource(invoice.getPdfContent());

        StringBuilder builder = new StringBuilder();
        builder.append("attachment; filename=invoice-").append(invoiceNumber).append(".pdf");
        String attachmentHeader = builder.toString();
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, attachmentHeader)
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
            RefundResponse responseRefund = new RefundResponse(refund.getId(), id, refund.getAmount(), refund.getRefundDate(), refund.getStatus().toString());

            return ResponseEntity.ok(responseRefund);
        } else {
            return ResponseEntity.ok(new MessageResponse("Return request has been rejected"));
        }
    }
    
    @PostMapping("/refunds/{id}/complete")
    public ResponseEntity<?> completeRefund(@PathVariable Long id) {
        getCurrentUser();
        
        Refund completedRefund = returnRefundService.completeRefund(id);

        RefundResponse responseCompletedRefund = new RefundResponse(completedRefund.getId(), id, completedRefund.getAmount(), completedRefund.getRefundDate(), completedRefund.getStatus().toString());

        return ResponseEntity.ok(responseCompletedRefund);
    }
}