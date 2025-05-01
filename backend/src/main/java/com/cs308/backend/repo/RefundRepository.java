package com.cs308.backend.repo;

import com.cs308.backend.dao.Refund;
import com.cs308.backend.dao.ReturnRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    Optional<Refund> findByReturnRequest(ReturnRequest returnRequest);
}