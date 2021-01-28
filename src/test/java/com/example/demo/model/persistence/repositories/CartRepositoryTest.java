package com.example.demo.model.persistence.repositories;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.utils.Data;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CartRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    //    @Autowired
    Data data;

    @Autowired
    CartRepository cartRepository;

    User user;


    @Before
    public void setUp() {
        data = new Data();
        List<Item> items = data.createItemList(3, entityManager);
        user = data.crateUser("sak", (long) 1);
        entityManager.persist(user);
        Cart cart = data.createCart((long) 0, items, user);
        cart.setUser(user);
        Object id = entityManager.persistAndGetId(cart);
        System.out.println(id);
        user.setCart(cart);
    }

    @Test
    public void test() {
        Optional<Cart> cart = cartRepository.findById((long) 1);
        assertNotNull(cart);
        assertEquals(3, cart.get().getItems().size());
        assertEquals(new BigDecimal(300), cart.get().getTotal());
        assertEquals("sak", cart.get().getUser().getUsername());
        assertEquals(1, cart.get().getUser().getId());
    }

    @Test
    public void test2() {
        Cart cart = cartRepository.findByUser(user);
        assertNotNull(cart);
        assertEquals(3, cart.getItems().size());
        assertEquals(new BigDecimal(300), cart.getTotal());
        assertEquals("sak", cart.getUser().getUsername());
    }

}
