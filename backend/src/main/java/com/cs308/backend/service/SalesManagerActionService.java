package com.cs308.backend.service;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.SalesManagerAction;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.SalesManagerActionRepository;

@Service
public class SalesManagerActionService {
    private final SalesManagerActionRepository repository;

    public SalesManagerActionService(SalesManagerActionRepository repository) {
        this.repository = repository;
    }

    public void logAction(User productManager, String actionType, String details) {
        SalesManagerAction action = new SalesManagerAction(productManager, actionType, details);
        repository.save(action);
    }
}
