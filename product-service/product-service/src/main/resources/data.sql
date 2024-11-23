-- Create product table
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL
);

-- Insert sample product data
INSERT INTO product (name, price, quantity) VALUES ('Product A', 20.0, 100);
INSERT INTO product (name, price, quantity) VALUES ('Product B', 10.0, 50);
INSERT INTO product (name, price, quantity) VALUES ('Product C', 30.0, 200);

-- Create cart table
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
    product_name VARCHAR(255) NOT NULL,
    product_price DOUBLE NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES cart(cart_id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

-- Insert sample cart data
INSERT INTO cart (total_price) VALUES (50);

-- Insert sample cart items
INSERT INTO cart_item (cart_id, product_id, quantity,product_name,product_price) VALUES (1, 1, 2, 'A',20);
INSERT INTO cart_item (cart_id, product_id, quantity,product_name,product_price) VALUES (1, 2, 1,'B',10);