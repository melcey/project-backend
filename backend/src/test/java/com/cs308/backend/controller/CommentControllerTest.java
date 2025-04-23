package com.cs308.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cs308.backend.dao.Comment;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.CommentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void testSubmitComment_Unauthenticated() throws Exception {
        mockMvc.perform(post("/comments/submit")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"text\": \"Test comment\"}"))
                .andExpect(status().isOk()); // Gerçek token'la testte mocklanması gerekir.
    }

    @Test
    @WithMockUser(roles = "PRODUCT_MANAGER")
    public void testApproveComment_Authorized() throws Exception {
        mockMvc.perform(post("/comments/approve")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SALES_MANAGER")
    public void testDisapproveComment_Authorized() throws Exception {
        mockMvc.perform(post("/comments/disapprove")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PRODUCT_MANAGER")
    public void testDeleteComment_Authorized() throws Exception {
        mockMvc.perform(delete("/comments/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1}"))
                .andExpect(status().isOk());
    }
}
