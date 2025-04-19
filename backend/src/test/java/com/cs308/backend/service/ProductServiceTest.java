package com.cs308.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cs308.backend.dao.Category;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.CategoryRepository;
import com.cs308.backend.repo.ProductRepository;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Category testCategory;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Electronics");
        testCategory.setDescription("Electronic devices and gadgets.");

        testUser = new User();
        testUser.setId(1L);
        testUser.setName("John Doe");

        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Smartphone");
        testProduct.setModel("Galaxy S21");
        testProduct.setSerialNumber("SN123456");
        testProduct.setDescription("A high-end smartphone.");
        testProduct.setQuantityInStock(50);
        testProduct.setPrice(BigDecimal.valueOf(999.99));
        testProduct.setWarrantyStatus("1 Year");
        testProduct.setDistributorInfo("Samsung");
        testProduct.setIsActive(true);
        testProduct.setCategory(testCategory);
        testProduct.setProductManager(testUser);
    }

    @Test
    void testFindAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(testProduct));

        List<Product> products = productService.findAllProducts();

        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getName()).isEqualTo("Smartphone");
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Optional<Product> product = productService.findProductById(1L);

        assertThat(product).isPresent();
        assertThat(product.get().getName()).isEqualTo("Smartphone");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindProductsByName() {
        when(productRepository.findByName("Smartphone")).thenReturn(List.of(testProduct));
        when(productRepository.findByNameContains("Smart")).thenReturn(List.of(testProduct));

        List<Product> products = productService.findProductsByName("Smartphone");

        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getName()).isEqualTo("Smartphone");
        verify(productRepository, times(1)).findByName("Smartphone");
        verify(productRepository, times(1)).findByNameContains("Smartphone");
    }

    @Test
    void testFindProductsByCategory() {
        when(productRepository.findByCategoryId(1L)).thenReturn(List.of(testProduct));

        List<Product> products = productService.findProductsByCategory(testCategory);

        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getCategory().getName()).isEqualTo("Electronics");
        verify(productRepository, times(1)).findByCategoryId(1L);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.insertNewProduct(testProduct)).thenReturn(Optional.of(testProduct));

        Optional<Product> createdProduct = productService.createProduct(testProduct);

        assertThat(createdProduct).isPresent();
        assertThat(createdProduct.get().getName()).isEqualTo("Smartphone");
        verify(productRepository, times(1)).insertNewProduct(testProduct);
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doNothing().when(productRepository).deleteProduct(testProduct);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).deleteProduct(testProduct);
    }

    @Test
    void testUpdateProductName() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.updateProductName(testProduct, "Updated Smartphone")).thenAnswer(invocation -> {
            testProduct.setName("Updated Smartphone");
            return Optional.of(testProduct);
        });

        Optional<Product> updatedProduct = productService.updateProductName(1L, "Updated Smartphone");

        assertThat(updatedProduct).isPresent();
        assertThat(updatedProduct.get().getName()).isEqualTo("Updated Smartphone");
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).updateProductName(testProduct, "Updated Smartphone");
    }

    @Test
    void testFindManagedProductsByName() {
        when(productRepository.findManagedByName("Smartphone", 1L)).thenReturn(List.of(testProduct));
        when(productRepository.findManagedByNameContains("Smart", 1L)).thenReturn(List.of(testProduct));

        List<Product> products = productService.findManagedProductsByName("Smartphone", testUser);

        assertThat(products).isNotEmpty();
        assertThat(products.get(0).getName()).isEqualTo("Smartphone");
        verify(productRepository, times(1)).findManagedByName("Smartphone", 1L);
        verify(productRepository, times(1)).findManagedByNameContains("Smartphone", 1L);
    }

    @Test
    void testUpdateProductCategory() {
        // Create a different category to simulate the initial state
        Category initialCategory = new Category();
        initialCategory.setId(2L);
        initialCategory.setName("Home Appliances");
        initialCategory.setDescription("Appliances for home use.");
    
        // Set the initial category for the test product
        testProduct.setCategory(initialCategory);
    
        // Mock behavior for finding the category and product
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory)); // The new category
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
    
        // Use thenAnswer to simulate updating the product's category
        when(productRepository.updateProductCategory(testProduct, testCategory)).thenAnswer(invocation -> {
            testProduct.setCategory(testCategory); // Update the category
            return Optional.of(testProduct);
        });
    
        // Call the service method
        Optional<Product> updatedProduct = productService.updateProductCategory(1L, 1L);
    
        // Assertions
        assertThat(updatedProduct).isPresent();
        assertThat(updatedProduct.get().getCategory()).isNotNull();
        assertThat(updatedProduct.get().getCategory().getName()).isEqualTo("Electronics"); // New category
        assertThat(updatedProduct.get().getCategory().getId()).isEqualTo(1L); // New category ID
    
        // Verify interactions with the repositories
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).updateProductCategory(testProduct, testCategory);
    }
}