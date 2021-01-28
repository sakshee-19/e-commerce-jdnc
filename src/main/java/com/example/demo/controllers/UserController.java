package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        logger.info("findById User id={}", id);
        return ResponseEntity.of(userRepository.findById(id));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        logger.info("**START** findByUserName userName={}", username);
        User user = userRepository.findByUsername(username);
        logger.info("**END** findByUserName");
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        logger.info("**START** createUser username={}", createUserRequest.getUsername());
        User user = new User();
        user.setUsername(createUserRequest.getUsername());
        String password = createUserRequest.getPassword();
        if (password.length() < 7 || !password.equals(createUserRequest.getConfirmPassword())) {
            logger.error("Error with user password can not create user");
            return ResponseEntity.badRequest().build();
        }
        logger.info("Password criteria check passed");
        user.setPassword(bCryptPasswordEncoder.encode(password));
        logger.info("saving cart");
        Cart cart = new Cart();
        cartRepository.save(cart);
        user.setCart(cart);
        logger.info("Saving User");
        userRepository.save(user);
        logger.info("**END** createUser username={}", createUserRequest.getUsername());
        return ResponseEntity.ok(user);
    }

}
