Feature: Cart API

  Scenario: Add product to cart
    Given a cart exists with ID 1
    When I add product with ID 1 to the cart with quantity 2
    Then the cart should have a product with ID 1 and quantity 2

  Scenario: Remove product from cart
    Given a cart exists with ID 1 containing product with ID 1 and quantity 3
    When I remove 2 quantity of product with ID 1 from the cart
    Then the cart should have a product with ID 1 and quantity 1

  Scenario: View cart items
    Given a cart exists with ID 1 containing product with ID 1 and quantity 2
    When I view the cart with ID 1
    Then I should see the product with ID 1 and quantity 2
