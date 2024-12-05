Feature: Product API

  Scenario: Retrieve all products
    Given the product repository contains products
    When I request to get all products
    Then I should receive a list of products

  Scenario: Retrieve a product by ID
    Given the product repository contains a product with ID 1
    When I request to get the product with ID 1
    Then I should receive the product with ID 1

  Scenario: Update a product by ID
    Given the product repository contains a product with ID 1
    When I request to update the product with ID 1
    Then the product with ID 1 should be updated