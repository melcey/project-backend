package com.cs308.backend.service;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.ProductManagerAction;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.ProductManagerActionRepository;

@Service
public class ProductManagerActionService {
    private final ProductManagerActionRepository repository;

    public ProductManagerActionService(ProductManagerActionRepository repository) {
        this.repository = repository;
    }

    public void logAction(User productManager, String actionType, String details) {
        ProductManagerAction action = new ProductManagerAction(productManager, actionType, details);
        repository.save(action);
    }
}
