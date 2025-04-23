package com.cs308.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cs308.backend.dao.Category;
import com.cs308.backend.dao.Product;
import com.cs308.backend.service.ProductService;

@AutoConfigureMockMvc
public class ProductControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testGetProductById_Success() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test Product");
        mockProduct.setModel("Model X");
        mockProduct.setSerialNumber("12345");
        mockProduct.setDescription("Test Description");
        mockProduct.setQuantityInStock(10);
        mockProduct.setPrice(BigDecimal.valueOf(99.99));
        mockProduct.setWarrantyStatus("1 Year");
        mockProduct.setDistributorInfo("Test Distributor");
        mockProduct.setIsActive(true);
        mockProduct.setImageUrl("http://example.com/image.jpg");
        mockProduct.setCategory(new Category("Category 1", "Category Description"));

        when(productService.findProductById(1L)).thenReturn(Optional.of(mockProduct));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.model").value("Model X"));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        when(productService.findProductById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllProducts() throws Exception {
        Product mockProduct1 = new Product();
        mockProduct1.setId(1L);
        mockProduct1.setName("Product 1");
        mockProduct1.setCategory(new Category("Category 1", "Description 1"));

        Product mockProduct2 = new Product();
        mockProduct2.setId(2L);
        mockProduct2.setName("Product 2");
        mockProduct2.setCategory(new Category("Category 2", "Description 2"));

        List<Product> mockProducts = Arrays.asList(mockProduct1, mockProduct2);

        when(productService.findAllProducts()).thenReturn(mockProducts);

        mockMvc.perform(get("/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products[0].id").value(1))
                .andExpect(jsonPath("$.products[1].id").value(2));
    }

    @Test
    void testSearchProducts() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test Product");
        mockProduct.setCategory(new Category("Category 1", "Description 1"));
        mockProduct.setIsActive(true);
        mockProduct.setModel("Model X");
        mockProduct.setSerialNumber("12345");
        mockProduct.setDescription("Test Description");
        mockProduct.setQuantityInStock(10);
        mockProduct.setPrice(BigDecimal.valueOf(99.99));
        mockProduct.setWarrantyStatus("1 Year");
        mockProduct.setDistributorInfo("Test Distributor");
        mockProduct.setImageUrl("http://example.com/image.jpg");

        List<Product> mockProducts = Arrays.asList(mockProduct);

        when(productService.searchProducts(anyString(), anyString(), anyString(), anyString(), anyString(), anyBoolean(),
                anyString(), any(BigDecimal.class), any(BigDecimal.class), anyInt(), anyInt(), anyList()))
                .thenReturn(mockProducts);

        mockMvc.perform(get("/products")
                        .param("name", "Test%20Product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products[0].id").value(1L))
                .andExpect(jsonPath("$.products[0].name").value("Test Product"));
    }
}