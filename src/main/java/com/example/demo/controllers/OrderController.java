package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;


    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrder> submit(@PathVariable String username) {
        logger.info("**START** submit Order username={}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.info("User with username={} does not exists", username);
            return ResponseEntity.notFound().build();
        }
        logger.info("User with username={} found", username);
        UserOrder order = UserOrder.createFromCart(user.getCart());
        logger.info("save order");
        orderRepository.save(order);
        logger.info("**END** submitOrder");
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
        logger.info("**START** getOrdersForUser username={}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.info("User with username={} does not exists", username);
            return ResponseEntity.notFound().build();
        }
        logger.info("User with username={} found", username);
        logger.info("**END** getOrdersForUser ");
        return ResponseEntity.ok(orderRepository.findByUser(user));
    }
}
