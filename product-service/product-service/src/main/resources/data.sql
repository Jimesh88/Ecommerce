-- product-service's data.sql
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    quantity INT NOT NULL
);

-- Sample product data
INSERT INTO product (name, price, quantity) VALUES ('Product A', 20.9, 100);
INSERT INTO product (name, price, quantity) VALUES ('Product B', 15.5, 50);
INSERT INTO product (name, price, quantity) VALUES ('Product C', 30.0, 200);
