package com.cs308.backend.service;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Rating;
import com.cs308.backend.repo.RatingRepository;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }
    Rating submitRating(Rating ratingToSubmit){
        Rating submittedRating = ratingRepository.save(ratingToSubmit);

        return submittedRating;
    }

    void deleteRating(Rating ratingToDelete){
        ratingRepository.delete(ratingToDelete);
    }
}
