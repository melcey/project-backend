package com.cs308.backend.controller;

import com.cs308.backend.dao.Order;
import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.ReturnRequest;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.CreateReturnRequest;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.ReturnRefundService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class CustomerReturnControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReturnRefundService returnRefundService;

    @InjectMocks
    private CustomerReturnController controller;

    private ObjectMapper objectMapper;

    private User testUser;
    private Product testProduct;
    private Order testOrder;
    private ReturnRequest mockReturnRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();

        // mock user
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setRole(Role.customer);

        // mock product
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Mock Product");

        // mock order
        testOrder = new Order();
        testOrder.setId(123L);
        testOrder.setUser(testUser);

        // mock return request
        mockReturnRequest = new ReturnRequest();
        mockReturnRequest.setId(99L);
        mockReturnRequest.setOrder(testOrder);
        mockReturnRequest.setUser(testUser);
        mockReturnRequest.setProduct(testProduct);
        mockReturnRequest.setQuantity(2);
        mockReturnRequest.setOriginalPrice(BigDecimal.valueOf(100));
        mockReturnRequest.setReason("Defective");

        // set authentication
        UserPrincipal principal = UserPrincipal.create(testUser);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testCreateReturnRequest_Success() throws Exception {
        CreateReturnRequest request = new CreateReturnRequest(123L, 1L, 2, "Defective");

        when(returnRefundService.createReturnRequest(any(User.class), anyLong(), anyLong(), anyInt(), anyString()))
                .thenReturn(mockReturnRequest);

        mockMvc.perform(post("/returns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testCreateReturnRequest_InternalServerError() throws Exception {
        CreateReturnRequest request = new CreateReturnRequest(123L, 1L, 2, "Defective");

        when(returnRefundService.createReturnRequest(any(User.class), anyLong(), anyLong(), anyInt(), anyString()))
                .thenThrow(new IllegalArgumentException("Invalid quantity"));

        mockMvc.perform(post("/returns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testGetUserReturnRequests_Success() throws Exception {
        when(returnRefundService.getUserReturnRequests(any(User.class)))
                .thenReturn(List.of(mockReturnRequest));

        mockMvc.perform(get("/returns"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALES_MANAGER")
    public void testGetUserReturnRequests_Forbidden() throws Exception {
        User manager = new User();
        manager.setId(2L);
        manager.setName("Manager");
        manager.setRole(Role.sales_manager);
        UserPrincipal principal = UserPrincipal.create(manager);
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(auth);
        SecurityContextHolder.setContext(ctx);

        mockMvc.perform(get("/returns"))
                .andExpect(status().isForbidden());
    }
}
