package com.cs308.backend.controller;

import com.cs308.backend.dao.*;
import com.cs308.backend.dto.CategoryResponse;
import com.cs308.backend.dto.ProductResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductManagerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @Mock
    private CommentService commentService;

    @Mock
    private RatingService ratingService;

    @Mock
    private ProductManagerActionService actionService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductManagerController productManagerController;

    private User mockUser;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productManagerController).build();

        // Mock user
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Role.product_manager);

        // Mock product
        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setName("Test Product");
        mockProduct.setModel("Model X");
        mockProduct.setSerialNumber("SN-123");
        mockProduct.setDescription("Sample Desc");
        mockProduct.setPrice(new BigDecimal("199.99"));
        mockProduct.setQuantityInStock(10);
        mockProduct.setWarrantyStatus("1 year");
        mockProduct.setDistributorInfo("ACME Inc.");
        mockProduct.setIsActive(true);
        mockProduct.setImageUrl("/uploads/img.jpg");
        mockProduct.setProductManager(mockUser);

        Category category = new Category("Electronics", "Gadgets");
        category.setId(5L);
        mockProduct.setCategory(category);

        // Set authentication context
        UserPrincipal userPrincipal = UserPrincipal.create(mockUser);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Test
    public void testGetManagedProductById_Success() throws Exception {
        when(productService.findManagedProductById(1L, mockUser)).thenReturn(Optional.of(mockProduct));

        mockMvc.perform(get("/prodman/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetManagedProductById_NotFound() throws Exception {
        when(productService.findManagedProductById(99L, mockUser)).thenReturn(Optional.empty());

        mockMvc.perform(get("/prodman/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetManagedProductById_UnauthorizedUser() throws Exception {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setRole(Role.product_manager);
        mockProduct.setProductManager(otherUser);

        when(productService.findManagedProductById(1L, mockUser)).thenReturn(Optional.of(mockProduct));

        mockMvc.perform(get("/prodman/1"))
                .andExpect(status().isForbidden());
    }
}
