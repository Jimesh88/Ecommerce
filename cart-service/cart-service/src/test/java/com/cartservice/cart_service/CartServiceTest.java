package com.cartservice.cart_service;

import com.cartservice.cart_service.dto.CartResponse;
import com.cartservice.cart_service.model.Cart;
import com.cartservice.cart_service.model.CartItem;
import com.cartservice.cart_service.repository.CartItemRepository;
import com.cartservice.cart_service.repository.CartRepository;
import com.cartservice.cart_service.service.CartService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.productservice.product_service.model.Product;
import static org.junit.jupiter.api.Assertions.*; // Import added for assertEquals
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.io.IOException;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@SpringBootTest
class CartServiceTest {
    @Autowired
    private CartService cartService;
    @MockBean
    private CartRepository cartRepository;
    @MockBean
    private CartItemRepository cartItemRepository;
    @MockBean
    private WebClient.Builder webClientBuilder;
    private WireMockServer wireMockServer;
    @BeforeEach
    void setUp() throws IOException {
        wireMockServer = new WireMockServer(8089); // Start WireMock server on port 8089
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8089")
                .build();
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        // Stub the WireMock server to return a successful response for the GET product endpoint
        WireMock.stubFor(WireMock.get(urlPathMatching("/1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"price\": 20.0}") ) );

        WireMock.stubFor(WireMock.put(urlPathMatching("/1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"price\": 20.0}") ) );
    }
    @AfterEach
    void tearDown() throws IOException {
        wireMockServer.stop();
    }
    @Test
    void testAddItemToCart() {
        Long cartId = 1L;
        Long productId = 1L;
        int quantity = 2;
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setTotalPrice(0.0);
        Product product = new Product();
        product.setId(productId);
        product.setName("Product A");
        product.setPrice(20.0);
        product.setQuantity(100);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCartId(cartId);
        cartItem.setProductId(productId);
        cartItem.setProductPrice(20.0);
        cartItem.setProductName("Product A");
        cartItem.setQuantity(quantity);
        WireMock.stubFor(WireMock.get(urlPathMatching("/1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"Product A\", \"price\": 20.0, \"quantity\": 100}")
                )
        );
        WireMock.stubFor(WireMock.put(urlPathMatching("/1"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": 1, \"name\": \"Product A\", \"price\": 20.0, \"quantity\": 98}")
                )
        );
        when(cartRepository.findById(cartId)).thenReturn(Mono.just(cart));
        when(cartItemRepository.findByCartId(cartId)).thenReturn(Flux.just(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(Mono.just(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(cart));
        Mono<CartResponse> responseMono = cartService.addProductToCart(cartId, productId, quantity);
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.getCart().getCartId() == cartId)
                .verifyComplete();
    }

    @Test void testRemoveProductFromCart() {
        Long cartId = 1L;
        Long productId = 1L;
        int quantity = 2;
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setTotalPrice(40.0);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCartId(cartId);
        cartItem.setProductId(productId);
        cartItem.setProductName("Product A");
        cartItem.setProductPrice(20.0);
        cartItem.setQuantity(3);
        Product product = new Product();
        product.setId(productId);
        product.setPrice(20.0);
        WireMock.stubFor(WireMock.get(urlPathMatching("/" + productId)).willReturn(WireMock.aResponse().withHeader("Content-Type", "application/json").withBody("{\"id\": 1, \"price\": 20.0}")));
        when(cartRepository.findById(cartId)).thenReturn(Mono.just(cart));
        when(cartItemRepository.findByCartId(cartId)).thenReturn(Flux.just(cartItem));
        when(cartItemRepository.delete(any(CartItem.class))).thenReturn(Mono.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(Mono.just(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(cart));
        Mono < CartResponse > responseMono = cartService.removeProductFromCart(cartId, productId, quantity);
        StepVerifier.create(responseMono).expectNextMatches(response -> {
                assertEquals(cartId, response.getCart().getCartId());
                assertEquals(0.0, response.getCart().getTotalPrice());
        return true;
        }).verifyComplete();
    }
    @Test
    void testViewCartItems() {
        Long cartId = 1L;
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setTotalPrice(50.0);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCartId(cartId);
        cartItem.setProductId(1L);
        cartItem.setProductName("Product A");
        cartItem.setProductPrice(20.0);
        cartItem.setQuantity(2);
        when(cartRepository.findById(cartId)).thenReturn(Mono.just(cart));
        when(cartItemRepository.findByCartId(cartId)).thenReturn(Flux.just(cartItem));
        Mono<CartResponse> responseMono = cartService.viewCartItems(cartId);
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.getCart().getCartId() == cartId)
                .verifyComplete();
    }
}
