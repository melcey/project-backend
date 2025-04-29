-- V14__Create_credit_cards_table.sql
CREATE TABLE credit_cards (
    card_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    card_name_holder TEXT NOT NULL,
    card_number BYTEA NOT NULL,
    expiry_month SMALLINT,
    expiry_year SMALLINT,
    cvv BYTEA NOT NULL
);