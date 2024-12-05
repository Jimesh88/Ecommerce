package com.productservice.product_service.steps;

import com.productservice.product_service.model.Product;
import com.productservice.product_service.repository.ProductRepository;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class ProductSteps {

    @Autowired
    private ProductRepository productRepository;

    private Flux<Product> productFlux;
    private Mono<Product> productMono;

    @Before
    public void setUp() {
        productRepository.deleteAll().block();
    }

    @Given("the product repository contains products")
    public void theProductRepositoryContainsProducts() {
        productRepository.saveAll(Flux.just(
                new Product(1L, "Product A", 20.0, 100),
                new Product(2L, "Product B", 30.0, 50)
        )).blockLast();
    }

    @Given("the product repository contains a product with ID {long}")
    public void theProductRepositoryContainsAProductWithID(long productId) {
        productRepository.save(new Product(productId, "Product A", 20.0, 100)).block();
    }

    @When("I request to get all products")
    public void iRequestToGetAllProducts() {
        productFlux = productRepository.findAll();
    }

    @When("I request to get the product with ID {long}")
    public void iRequestToGetTheProductWithID(long productId) {
        productMono = productRepository.findById(productId);
    }

    @When("I request to update the product with ID {long}")
    public void iRequestToUpdateTheProductWithID(long productId) {
        productMono = productRepository.findById(productId)
                .flatMap(product -> {
                    product.setQuantity(80);
                    return productRepository.save(product);
                });
    }

    @Then("I should receive a list of products")
    public void iShouldReceiveAListOfProducts() {
        StepVerifier.create(productFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Then("I should receive the product with ID {long}")
    public void iShouldReceiveTheProductWithID(long productId) {
        StepVerifier.create(productMono)
                .expectNextMatches(product -> product.getId().equals(productId))
                .verifyComplete();
    }

    @Then("the product with ID {long} should be updated")
    public void theProductWithIDShouldBeUpdated(long productId) {
        StepVerifier.create(productMono)
                .expectNextMatches(product -> product.getId().equals(productId) && product.getQuantity() == 80)
                .verifyComplete();
    }
}
