package com.example.demo.utils;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Data {

    public Cart createCart(Long id, List<Item> items, User user) {
        Cart cart = new Cart();
        cart.setId(id);
        cart.setItems(items);
        cart.setTotal(new BigDecimal(items.size() * 100));
        cart.setUser(user);
        return cart;
    }


    public Item createItem(Long id) {
        Item item = new Item();
//        item.setId(id);
        item.setName("nam" + id);
        item.setPrice(new BigDecimal(100));
        item.setDescription("name " + id);
        return item;
    }

    public List<Item> createItemList(int n, TestEntityManager entityManager){
        List<Item> it = new ArrayList<>();
        for(int i=0; i<n; ++i){
            Item itm = createItem((long)i);
            if(entityManager != null){
                entityManager.persist(itm);
            }
            it.add(itm);
        }
        return it;
    }

    public User crateUser(String name, Long id) {
        User user = new User();
        user.setUsername(name);
//        user.setId(id);
        return user;
    }


    public UserOrder crateOrder(Long id, List<Item> items, BigDecimal total, User user) {
        UserOrder order = new UserOrder();
        order.setId(id);
        order.setItems(items);
        order.setTotal(total);
        order.setUser(user);
        return order;
    }
}
