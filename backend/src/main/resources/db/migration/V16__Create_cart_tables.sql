-- V16__Create_cart_tables.sql

-- Main cart table
CREATE TABLE carts (
    cart_id SERIAL PRIMARY KEY,
    user_id INT UNIQUE NOT NULL REFERENCES users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10, 2) DEFAULT 0.00
);

-- Cart items table
CREATE TABLE cart_items (
    cart_item_id SERIAL PRIMARY KEY,
    cart_id INT NOT NULL REFERENCES carts(cart_id) ON DELETE CASCADE,
    product_id INT NOT NULL REFERENCES products(product_id) ON DELETE RESTRICT,
    quantity INT NOT NULL CHECK (quantity > 0),
    price_at_addition DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_product_in_cart UNIQUE (cart_id, product_id)
);

-- Indexes for performance
CREATE INDEX idx_carts_user ON carts(user_id);
CREATE INDEX idx_cart_items_cart ON cart_items(cart_id);
CREATE INDEX idx_cart_items_product ON cart_items(product_id);