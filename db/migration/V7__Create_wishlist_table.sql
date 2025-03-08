-- V7__Create_wishlist_table.sql
CREATE TABLE wishlist (
    wishlist_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    product_id INT REFERENCES products(product_id)
);

ALTER TABLE wishlist OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE wishlist TO user_service_role;
GRANT USAGE, SELECT ON SEQUENCE wishlist_wishlist_id_seq TO user_service_role;