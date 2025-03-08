CREATE TABLE credit_cards (
    card_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    card_number BYTEA,
    expiry_date BYTEA,
    cvv BYTEA
);

ALTER TABLE credit_cards OWNER TO user_service_role;
GRANT ALL PRIVILEGES ON TABLE credit_cards TO user_service_role;
GRANT USAGE, SELECT ON SEQUENCE credit_cards_card_id_seq TO user_service_role;