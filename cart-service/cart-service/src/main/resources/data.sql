CREATE TABLE cart (
    cart_id BIGINT PRIMARY KEY,
    total_price DECIMAL(10, 2)
);

CREATE TABLE cart_product (
    cart_id BIGINT,
    product_id BIGINT,
    FOREIGN KEY (cart_id) REFERENCES cart(cart_id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    PRIMARY KEY (cart_id, product_id)
);
