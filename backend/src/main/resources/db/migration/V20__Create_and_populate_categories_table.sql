-- V21__Create_and_populate_categories_table.sql

-- Insert initial categories
INSERT INTO categories (name, description)
VALUES
    ('Electronics', 'Devices and gadgets for everyday use.'),       -- category_id = 1
    ('Home Appliances', 'Appliances for home and kitchen use.'),   -- category_id = 2
    ('Clothing', 'Fashionable apparel for all occasions.'),        -- category_id = 3
    ('Books', 'Books for reading and learning.'),                  -- category_id = 4
    ('Toys', 'Fun and educational toys for kids.'),                -- category_id = 5
    ('Sports', 'Equipment and gear for sports and fitness.'),      -- category_id = 6
    ('Furniture', 'Stylish and functional furniture for your home.'), -- category_id = 7
    ('Health & Beauty', 'Products for health, wellness, and beauty.'), -- category_id = 8
    ('Automotive', 'Parts and accessories for vehicles.'),         -- category_id = 9
    ('Garden', 'Tools and plants for gardening enthusiasts.');     -- category_id = 10