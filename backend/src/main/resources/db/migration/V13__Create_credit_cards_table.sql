-- V13__Create_credit_cards_table.sql
CREATE TABLE credit_cards (
    card_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    card_number BYTEA,
    expiry_date BYTEA,
    cvv BYTEA
);