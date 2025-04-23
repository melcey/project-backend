package com.cs308.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.Rating;
import com.cs308.backend.dao.User;
import com.cs308.backend.repo.RatingRepository;

public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubmitRating() {
        // Mock data
        Rating rating = new Rating();
        when(ratingRepository.save(rating)).thenReturn(rating);

        // Call the service method
        Optional<Rating> submittedRating = ratingService.submitRating(rating);

        // Assert
        assertTrue(submittedRating.isPresent());
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    public void testFindRatingsForProduct() {
        // Mock data
        Product product = new Product();
        Rating rating1 = new Rating();
        Rating rating2 = new Rating();
        when(ratingRepository.findByRatedProduct(product)).thenReturn(Arrays.asList(rating1, rating2));

        // Call the service method
        List<Rating> ratings = ratingService.findRatingsForProduct(product);

        // Assert
        assertEquals(2, ratings.size());
        verify(ratingRepository, times(1)).findByRatedProduct(product);
    }

    @Test
    public void testFindRatingsByUser() {
        // Mock data
        User user = new User();
        Rating rating1 = new Rating();
        Rating rating2 = new Rating();
        when(ratingRepository.findByRatingUser(user)).thenReturn(Arrays.asList(rating1, rating2));

        // Call the service method
        List<Rating> ratings = ratingService.findRatingsByUser(user);

        // Assert
        assertEquals(2, ratings.size());
        verify(ratingRepository, times(1)).findByRatingUser(user);
    }
}