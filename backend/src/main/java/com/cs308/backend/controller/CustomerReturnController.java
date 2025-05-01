package com.cs308.backend.controller;

import com.cs308.backend.dao.ReturnRequest;
import com.cs308.backend.dao.Role;
import com.cs308.backend.dao.User;
import com.cs308.backend.dto.CreateReturnRequest;
import com.cs308.backend.dto.ReturnRequestResponse;
import com.cs308.backend.security.UserPrincipal;
import com.cs308.backend.service.ReturnRefundService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/returns")
public class CustomerReturnController {
    
    private final ReturnRefundService returnRefundService;
    
    public CustomerReturnController(ReturnRefundService returnRefundService) {
        this.returnRefundService = returnRefundService;
    }
    
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if ((auth == null) || (!(auth.isAuthenticated()))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }
        
        UserPrincipal userDetails = (UserPrincipal) auth.getPrincipal();
        User user = userDetails.getUser();
        
        if (user.getRole() != Role.customer) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not a customer");
        }
        
        return user;
    }
    
    @PostMapping
    public ResponseEntity<?> createReturnRequest(@RequestBody CreateReturnRequest request) {
        User customer = getCurrentUser();
        
        try {
            ReturnRequest returnRequest = returnRefundService.createReturnRequest(
                customer,
                request.getOrderId(),
                request.getProductId(),
                request.getQuantity(),
                request.getReason()
            );
            
            return ResponseEntity.ok(new ReturnRequestResponse(returnRequest));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getUserReturnRequests() {
        User customer = getCurrentUser();
        
        List<ReturnRequest> requests = returnRefundService.getUserReturnRequests(customer);
        List<ReturnRequestResponse> response = requests.stream()
            .map(ReturnRequestResponse::new)
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(response);
    }
}