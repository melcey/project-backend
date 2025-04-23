package com.cs308.backend.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Rating;
import com.cs308.backend.dao.User;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findById(Long id);

    List<Rating> findByRatedProduct(Product ratedProduct);

    List<Rating> findByRatingUser(User ratingUser);

    List<Rating> findByRating(int rating);
    
    List<Rating> findByRatingLessThanEqual(int rating);
    
    List<Rating> findByRatingGreaterThanEqual(int rating);

    List<Rating> findByRatingLessThanEqualAndRatingGreaterThanEqual(int minRating, int maxRating);

    List<Rating> findByRatedProductAndRating(Product ratedProduct, int rating);
    
    List<Rating> findByRatedProductAndRatingLessThanEqual(Product ratedProduct, int rating);
    
    List<Rating> findByRatedProductAndRatingGreaterThanEqual(Product ratedProduct, int rating);

    List<Rating> findByRatedProductAndRatingLessThanEqualAndRatingGreaterThanEqual(Product ratedProduct, int minRating, int maxRating);

    List<Rating> findByRatingDate(LocalDateTime ratingDate);

    List<Rating> findByRatingDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Rating> findByRatingDateBefore(LocalDateTime beforeDate);

    List<Rating> findByRatingDateAfter(LocalDateTime afterDate);
}
