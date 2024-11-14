

CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL
);

INSERT INTO product (name, price) VALUES ('Product A', 20.9);
INSERT INTO product (name, price) VALUES ('Product B', 20);
INSERT INTO product (name, price) VALUES ('Product C', 30);