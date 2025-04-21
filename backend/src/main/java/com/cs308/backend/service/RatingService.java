package com.cs308.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Rating;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.RatingRepository;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Optional<Rating> submitRating(Rating ratingToSubmit){
        try {
            Rating submittedRating = ratingRepository.save(ratingToSubmit);
            return Optional.of(submittedRating);
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Rating> findRatingsForProduct(Product product) {
        return ratingRepository.findByRatedProduct(product);
    }

    public List<Rating> findRatingsByUser(User user) {
        return ratingRepository.findByRatingUser(user);
    }
}
