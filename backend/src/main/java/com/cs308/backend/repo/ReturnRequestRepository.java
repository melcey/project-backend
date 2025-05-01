package com.cs308.backend.repo;

import com.cs308.backend.dao.ReturnRequest;
import com.cs308.backend.dao.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Long> {
    
    List<ReturnRequest> findByUser(User user);
    
    List<ReturnRequest> findByStatus(ReturnRequest.Status status);
    
    @Query("SELECT r FROM ReturnRequest r WHERE r.status = 'PENDING'")
    List<ReturnRequest> findAllPendingRequests();
}