package com.cs308.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.Rating;

public interface RatingRepository extends JpaRepository<Rating,Long>{

}
