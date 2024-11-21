-- Create product table in cart-service database

-- Create cart table in cart-service database
CREATE TABLE IF NOT EXISTS cart (
    cart_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_price DOUBLE NOT NULL
);

-- Create cart_item table referencing product and cart tables
CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BIGINT,
    product_id BIGINT,
    quantity INT,
    FOREIGN KEY (cart_id) REFERENCES cart(cart_id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

INSERT INTO cart (total_price) VALUES (0.0);  -- Initial total price is 0

-- Insert sample cart items (product added to the cart)
-- Adding Product A with quantity 2
INSERT INTO cart_item (cart_id, product_id, quantity) VALUES (1, 1, 2);

-- Adding Product B with quantity 1
INSERT INTO cart_item (cart_id, product_id, quantity) VALUES (1, 2, 1);