package com.cartservice.cart_service.steps;

import com.cartservice.cart_service.dto.AddProductRequest;
import com.cartservice.cart_service.dto.CartResponse;
import com.cartservice.cart_service.model.Cart;
import com.cartservice.cart_service.model.CartItem;
import com.cartservice.cart_service.repository.CartRepository;
import com.cartservice.cart_service.repository.CartItemRepository;
import com.cartservice.cart_service.service.CartService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
public class CartSteps {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    private Mono<CartResponse> cartResponseMono;

    @Before
    public void setUp() {
        cartRepository.deleteAll().block();
        cartItemRepository.deleteAll().block();
    }

    @Given("a cart exists with ID {long}")
    public void aCartExistsWithID(long cartId) {
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setTotalPrice(0.0);
        cartRepository.save(cart).block();
    }

    @Given("a cart exists with ID {long} containing product with ID {long} and quantity {int}")
    public void aCartExistsWithIDContainingProductWithIDAndQuantity(long cartId, long productId, int quantity) {
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setTotalPrice(quantity * 20.0);
        cartRepository.save(cart).block();

        CartItem cartItem = new CartItem();
        cartItem.setCartId(cartId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        cartItem.setProductName("Product A");
        cartItem.setProductPrice(20.0);
        cartItemRepository.save(cartItem).block();
    }

    @When("I add product with ID {long} to the cart with quantity {int}")
    public void iAddProductWithIDToTheCartWithQuantity(long productId, int quantity) {
        AddProductRequest addProductRequest = new AddProductRequest();
        addProductRequest.setCartId(1L);
        addProductRequest.setProductId(productId);
        addProductRequest.setQuantity(quantity);

        cartResponseMono = cartService.addProductToCart(addProductRequest.getCartId(), addProductRequest.getProductId(), addProductRequest.getQuantity());
    }

    @When("I remove {int} quantity of product with ID {long} from the cart")
    public void iRemoveQuantityOfProductWithIDFromTheCart(int quantity, long productId) {
        AddProductRequest addProductRequest = new AddProductRequest();
        addProductRequest.setCartId(1L);
        addProductRequest.setProductId(productId);
        addProductRequest.setQuantity(quantity);

        cartResponseMono = cartService.removeProductFromCart(addProductRequest.getCartId(), addProductRequest.getProductId(), addProductRequest.getQuantity());
    }

    @When("I view the cart with ID {long}")
    public void iViewTheCartWithID(long cartId) {
        cartResponseMono = cartService.viewCartItems(cartId);
    }

    @Then("the cart should have a product with ID {long} and quantity {int}")
    public void theCartShouldHaveAProductWithIDAndQuantity(long productId, int quantity) {
        StepVerifier.create(cartResponseMono)
                .expectNextMatches(response -> response.getItems().stream()
                        .anyMatch(item -> item.getId().equals(productId) && item.getQuantity() == quantity))
                .verifyComplete();
    }

    @Then("I should see the product with ID {long} and quantity {int}")
    public void iShouldSeeTheProductWithIDAndQuantity(long productId, int quantity) {
        StepVerifier.create(cartResponseMono)
                .expectNextMatches(response -> response.getItems().stream()
                        .anyMatch(item -> item.getId().equals(productId) && item.getQuantity() == quantity))
                .verifyComplete();
    }
}
