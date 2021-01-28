package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
        logger.info("**START** addToCart");
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            logger.info("User with username={} does not exists", request.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("User with username={} found", user.getUsername());
        Optional<Item> item = itemRepository.findById(request.getItemId());
        if (!item.isPresent()) {
            logger.info("item with id={} does not exists", request.getItemId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Item with id={} found", request.getItemId());
        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.addItem(item.get()));
        if(cart.getItems()!=null)
            logger.info("final items size={}", cart.getItems().size());
        logger.info("saving the cart" );
        cartRepository.save(cart);
        logger.info("**END** addToCart");
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
        logger.info("**START** removeFromcart");
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            logger.info("User with username={} does not exists", request.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("User with username={} found", user.getUsername());
        Optional<Item> item = itemRepository.findById(request.getItemId());
        if (!item.isPresent()) {
            logger.info("item with id={} does not exists", request.getItemId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("Item with id={} found", request.getItemId());
        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity())
                .forEach(i -> cart.removeItem(item.get()));
        if(cart.getItems()!=null)
            logger.info("final items size={}", cart.getItems().size());
        logger.info("saving the cart" );
        cartRepository.save(cart);
        logger.info("**END** removeFromcart");
        return ResponseEntity.ok(cart);
    }

}
