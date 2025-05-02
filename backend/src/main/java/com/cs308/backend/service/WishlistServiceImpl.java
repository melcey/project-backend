package com.cs308.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cs308.backend.dao.Product;
import com.cs308.backend.dao.User;
import com.cs308.backend.dao.Wishlist;
import com.cs308.backend.dto.WishlistRequest;
import com.cs308.backend.dto.WishlistResponse;
import com.cs308.backend.repo.ProductRepository;
import com.cs308.backend.repo.WishlistRepository;
import com.cs308.backend.service.WishlistService;

@Service
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistServiceImpl(WishlistRepository wishlistRepository, ProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void addToWishlist(User user, WishlistRequest request) {
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (productOpt.isEmpty()) return;

        Product product = productOpt.get();

        // Aynı ürün zaten varsa tekrar ekleme
        if (wishlistRepository.findByUserAndProduct(user, product).isPresent()) return;

        Wishlist wishlistEntry = new Wishlist(user, product);
        wishlistRepository.save(wishlistEntry);
    }

    @Override
    public List<WishlistResponse> getWishlist(User user) {
        List<Wishlist> wishlist = wishlistRepository.findByUser(user);
        return wishlist.stream()
                .map(entry -> {
                    Product product = entry.getProduct();
                    return new WishlistResponse(
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            product.getImageUrl()
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public void removeFromWishlist(User user, Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) return;

        wishlistRepository.deleteByUserAndProduct(user, productOpt.get());
    }
}
