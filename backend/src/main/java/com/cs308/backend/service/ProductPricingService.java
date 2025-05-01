package com.cs308.backend.service;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductPricingService {
    private final ProductRepository productRepository;
    private final WishlistService wishlistService;
    private final EmailService emailService;
    private final InvoiceService invoiceService;
    private final OrderService orderService;
    
    public ProductPricingService(ProductRepository productRepository,
                               WishlistService wishlistService,
                               EmailService emailService,
                               InvoiceService invoiceService,
                               OrderService orderService) {
        this.productRepository = productRepository;
        this.wishlistService = wishlistService;
        this.emailService = emailService;
        this.invoiceService = invoiceService;
        this.orderService = orderService;
    }
    
    /**
     * Approves and sets the price for a product
     * @param productId ID of the product
     * @param price selling price to set
     * @param costPrice cost price (can be null)
     * @return updated product entity
     */
    public Product approveProductPrice(Long productId, BigDecimal price, BigDecimal costPrice) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        product.setPrice(price);
        product.setOriginalPrice(price);
        product.setCostPrice(costPrice != null ? costPrice : price.multiply(new BigDecimal("0.5")));
        product.setIsPriced(true);
        
        return productRepository.save(product);
    }
    
    /**
     * Applies a discount to multiple products
     * @param productIds list of product IDs to apply discount to
     * @param discountRate discount percentage (e.g., 20 for 20%)
     * @return list of updated products
     */
    public List<Product> applyDiscountToProducts(List<Long> productIds, BigDecimal discountRate) {
        List<Product> products = productRepository.findAllById(productIds);
        List<Product> updatedProducts = new ArrayList<>();
        
        for (Product product : products) {
            product.applyDiscount(discountRate);
            updatedProducts.add(productRepository.save(product));
            notifyWishlistUsers(product);
        }
        
        return updatedProducts;
    }
    
    /**
     * Removes discounts from multiple products
     * @param productIds list of product IDs to remove discounts from
     * @return list of updated products
     */
    public List<Product> removeDiscountsFromProducts(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        List<Product> updatedProducts = new ArrayList<>();
        
        for (Product product : products) {
            product.removeDiscount();
            updatedProducts.add(productRepository.save(product));
        }
        
        return updatedProducts;
    }
    
    /**
     * Notifies users who have a product in their wishlist about a discount
     * @param product product with applied discount
     */
    private void notifyWishlistUsers(Product product) {
        List<User> users = wishlistService.getUsersWithProductInWishlist(product.getId());
        for (User user : users) {
            String subject = "Discount on Your Wishlist Item";
            String text = String.format(
                "The product %s is now %.0f%% off! New price: $%.2f",
                product.getName(),
                product.getDiscountRate(),
                product.getPrice()
            );
            emailService.sendEmail(user.getEmail(), subject, text);
        }
    }
    
    /**
     * Updates the cost price of a product
     * @param productId ID of the product
     * @param costPrice new cost price
     * @return updated product entity
     */
    public Product updateProductCostPrice(Long productId, BigDecimal costPrice) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        product.setCostPrice(costPrice);
        return productRepository.save(product);
    }
    
    /**
     * Gets all products that have pricing information
     * @return list of priced products
     */
    public List<Product> getAllPricedProducts() {
        return productRepository.findByIsPricedTrue();
    }
    
    /**
     * Gets all products that still need pricing
     * @return list of unpriced products
     */
    public List<Product> getAllUnpricedProducts() {
        return productRepository.findByIsPricedFalse();
    }
    
    /**
     * Get invoices within a given date range
     * @param startDate starting date (inclusive)
     * @param endDate ending date (inclusive)
     * @return list of invoices in the date range
     */
    public List<Invoice> getInvoicesInDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay().minusNanos(1);
        return invoiceService.findInvoicesByDateRange(startDateTime, endDateTime);
    }
    
    /**
     * Calculate revenue and profit for a date range
     * @param startDate starting date (inclusive)
     * @param endDate ending date (inclusive)
     * @return map containing revenue, cost, profit, and profit margin
     */
    public Map<String, Object> calculateRevenueAndProfit(LocalDate startDate, LocalDate endDate) {
        List<Invoice> invoices = getInvoicesInDateRange(startDate, endDate);
        
        // Calculate totals
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        
        for (Invoice invoice : invoices) {
            totalRevenue = totalRevenue.add(invoice.getTotalAmount());
            
            // Calculate cost for each product in the order
            for (OrderItem item : invoice.getOrder().getOrderItems()) {
                Product product = item.getProduct();
                BigDecimal itemCost = (product.getCostPrice() != null) ? 
                    product.getCostPrice() : 
                    item.getPrice().multiply(new BigDecimal("0.5"));
                    
                totalCost = totalCost.add(itemCost.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        
        // Calculate profit and margin
        BigDecimal profit = totalRevenue.subtract(totalCost);
        BigDecimal profitMargin = totalRevenue.compareTo(BigDecimal.ZERO) > 0 ? 
            profit.divide(totalRevenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) : 
            BigDecimal.ZERO;
        
        Map<String, Object> results = new HashMap<>();
        results.put("revenue", totalRevenue);
        results.put("cost", totalCost);
        results.put("profit", profit);
        results.put("profitMargin", profitMargin);
        results.put("startDate", startDate);
        results.put("endDate", endDate);
        
        return results;
    }
    
    /**
     * Generate monthly revenue and profit data for charts
     * @param year the year to generate data for
     * @return list of monthly data points
     */
    public List<Map<String, Object>> generateMonthlyRevenueChart(int year) {
        List<Map<String, Object>> chartData = new ArrayList<>();
        
        for (int month = 1; month <= 12; month++) {
            LocalDate startOfMonth = LocalDate.of(year, month, 1);
            LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
            
            Map<String, Object> monthlyData = calculateRevenueAndProfit(startOfMonth, endOfMonth);
            monthlyData.put("month", month);
            chartData.add(monthlyData);
        }
        
        return chartData;
    }
}