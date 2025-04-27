package com.cs308.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class UploadConfig {
    @Value("${upload.product_images.dir}")
    private String productImgDirValue;

    public static String PRODUCT_IMG_DIR;

    @PostConstruct
    public void init() {
        PRODUCT_IMG_DIR = productImgDirValue;
    }
}
