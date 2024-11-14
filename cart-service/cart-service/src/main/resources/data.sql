

CREATE TABLE cart (
    cart_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT ,   -- The ID of the product in the cart
    total_price DECIMAL(10, 2) NOT NULL -- Total price of the cart, which is the price of a single product
);

INSERT INTO cart (cart_id, product_id, total_price) VALUES (1, 1, 20.9);  -- Cart 1, Product 10

