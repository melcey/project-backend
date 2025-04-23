package com.cs308.backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
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
import com.cs308.backend.dao.Comment;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Rating;
import com.cs308.backend.service.CommentService;
import com.cs308.backend.service.ProductService;
import com.cs308.backend.service.RatingService;

@AutoConfigureMockMvc
public class ProductControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private CommentService commentService;

    @Mock
    private RatingService ratingService;

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

        Category mockCategory = new Category("Category 1", "Category Description");
        mockCategory.setId(1L);
        mockProduct.setCategory(mockCategory);

        when(productService.findProductById(1L)).thenReturn(Optional.of(mockProduct));

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.model").value("Model X"))
                .andExpect(jsonPath("$.serialNumber").value("12345"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.quantityInStock").value(10))
                .andExpect(jsonPath("$.price").value(99.99))
                .andExpect(jsonPath("$.warrantyStatus").value("1 Year"))
                .andExpect(jsonPath("$.distributorInfo").value("Test Distributor"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.imageUrl").value("http://example.com/image.jpg"))
                .andExpect(jsonPath("$.category.id").value(1))
                .andExpect(jsonPath("$.category.name").value("Category 1"))
                .andExpect(jsonPath("$.category.description").value("Category Description"));
    }
    
    @Test
    void testGetCommentsForProduct_Success() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);

        Comment mockComment1 = new Comment();
        mockComment1.setId(1L);
        mockComment1.setComment("Great product!");

        Comment mockComment2 = new Comment();
        mockComment2.setId(2L);
        mockComment2.setComment("Not bad.");

        List<Comment> mockComments = Arrays.asList(mockComment1, mockComment2);

        when(productService.findProductById(1L)).thenReturn(Optional.of(mockProduct));
        when(commentService.findApprovedCommentsForProduct(mockProduct)).thenReturn(mockComments);

        mockMvc.perform(get("/products/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].comment").value("Great product!"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].comment").value("Not bad."));
    }

    @Test
    void testGetCommentsForProduct_NotFound() throws Exception {
        when(productService.findProductById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/1/comments"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetRatingsForProduct_Success() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);

        Rating mockRating1 = new Rating();
        mockRating1.setId(1L);
        mockRating1.setRating(5);

        Rating mockRating2 = new Rating();
        mockRating2.setId(2L);
        mockRating2.setRating(4);

        List<Rating> mockRatings = Arrays.asList(mockRating1, mockRating2);

        when(productService.findProductById(1L)).thenReturn(Optional.of(mockProduct));
        when(ratingService.findRatingsForProduct(mockProduct)).thenReturn(mockRatings);

        mockMvc.perform(get("/products/1/ratings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rating").value(5))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].rating").value(4));
    }

    @Test
    void testGetRatingsForProduct_NotFound() throws Exception {
        when(productService.findProductById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/1/ratings"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchProducts_NoFilters() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isEmpty());
    }

    @Test
    void testSearchProducts_WithFilters() throws Exception {
        Product mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Filtered Product");
        mockProduct.setIsActive(true);

        Category category = new Category("Filtered Category", "Filtered Description");
        category.setId(1L);
        mockProduct.setCategory(category);

        List<Product> mockProducts = Arrays.asList(mockProduct);

        when(productService.searchProducts(
            eq("Filtered Product"),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            eq(true),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            isNull(),
            eq(Arrays.asList(1L))
        )).thenReturn(mockProducts);

        mockMvc.perform(get("/products")
                        .param("name", "Filtered Product")
                        .param("isActive", "true")
                        .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products[0].id").value(1))
                .andExpect(jsonPath("$.products[0].name").value("Filtered Product"));
    }
}