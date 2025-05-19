package com.cs308.backend.controller;

import com.cs308.backend.dao.*;
import com.cs308.backend.dto.InvoiceResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class InvoiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    private User customerUser;
    private User productManagerUser;
    private Invoice invoice;
    private Order order;
    private Payment payment;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build();

        customerUser = new User();
        customerUser.setId(1L);
        customerUser.setRole(Role.customer);

        productManagerUser = new User();
        productManagerUser.setId(2L);
        productManagerUser.setRole(Role.product_manager);

        product = new Product();
        product.setId(100L);
        product.setProductManager(productManagerUser);

        OrderItem item = new OrderItem();
        item.setProduct(product);

        order = new Order();
        order.setId(10L);
        order.setUser(customerUser);
        order.setOrderItems(List.of(item));

        payment = new Payment();
        payment.setId(20L);
        payment.setOrder(order);

        invoice = new Invoice();
        invoice.setId(30L);
        invoice.setInvoiceNumber("INV123");
        invoice.setOrder(order);
        invoice.setPayment(payment);
        invoice.setInvoiceDate(LocalDateTime.now());
        invoice.setTotalAmount(BigDecimal.valueOf(500));
        invoice.setPdfContent("dummy-pdf-content".getBytes());
    }

    private void authenticate(User user) {
        UserPrincipal principal = UserPrincipal.create(user);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    public void testGetInvoice_Success_Customer() throws Exception {
        authenticate(customerUser);
        when(invoiceService.findByInvoiceNumber("INV123")).thenReturn(Optional.of(invoice));

        mockMvc.perform(get("/invoices/INV123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceNumber").value("INV123"));
    }

    @Test
    public void testGetInvoice_Success_ProductManager() throws Exception {
        authenticate(productManagerUser);
        when(invoiceService.findByInvoiceNumber("INV123")).thenReturn(Optional.of(invoice));

        mockMvc.perform(get("/invoices/INV123"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetInvoice_Forbidden_OtherCustomer() throws Exception {
        User otherCustomer = new User();
        otherCustomer.setId(999L);
        otherCustomer.setRole(Role.customer);
        authenticate(otherCustomer);
        when(invoiceService.findByInvoiceNumber("INV123")).thenReturn(Optional.of(invoice));

        mockMvc.perform(get("/invoices/INV123"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetInvoice_Forbidden_ProductManagerUnrelated() throws Exception {
        User unrelatedManager = new User();
        unrelatedManager.setId(999L);
        unrelatedManager.setRole(Role.product_manager);
        authenticate(unrelatedManager);
        when(invoiceService.findByInvoiceNumber("INV123")).thenReturn(Optional.of(invoice));

        mockMvc.perform(get("/invoices/INV123"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetInvoice_NotFound() throws Exception {
        authenticate(customerUser);
        when(invoiceService.findByInvoiceNumber("INV999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/invoices/INV999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDownloadInvoice_Success() throws Exception {
        authenticate(customerUser);
        when(invoiceService.findByInvoiceNumber("INV123")).thenReturn(Optional.of(invoice));

        mockMvc.perform(get("/invoices/INV123/download"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=invoice-INV123.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }

    @Test
    public void testDownloadInvoice_Forbidden_OtherCustomer() throws Exception {
        User other = new User();
        other.setId(88L);
        other.setRole(Role.customer);
        authenticate(other);
        when(invoiceService.findByInvoiceNumber("INV123")).thenReturn(Optional.of(invoice));

        mockMvc.perform(get("/invoices/INV123/download"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDownloadInvoice_NotFound() throws Exception {
        authenticate(customerUser);
        when(invoiceService.findByInvoiceNumber("INV999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/invoices/INV999/download"))
                .andExpect(status().isNotFound());
    }
}
