package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.utils.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderControllerIntgTest {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String url;

    private Data data;

    List<Item> items;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserRepository userRepository;

    User user;

    Cart cart;

    public ModifyCartRequest modifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("sakshee");
        modifyCartRequest.setItemId(1);
        modifyCartRequest.setQuantity(3);
        return modifyCartRequest;
    }

    @Before
    public void setUp() {
        url = "http://localhost:" + port + "/api/order";
        data = new Data();
        items = data.createItemList(2, null);
        items.get(0).setId(1L);
        user = data.crateUser("sakshee", 1L);
        cart = data.createCart(1L, items, user);
        user.setCart(cart);

        when(userRepository.findByUsername("sakshee")).thenReturn(user);

//        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(items.get(0)));
//        when(itemRepository.findByName("nam0")).thenReturn(items);
//
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(userOrder(user)));
    }

    public UserOrder userOrder(User user) {
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        userOrder.setTotal(new BigDecimal(200));
        userOrder.setItems(items);
        return userOrder;
    }

    @Test
    public void submitTest() {
        ResponseEntity<UserOrder> responseEntity = testRestTemplate.postForEntity(url + "/submit/sakshee", null, UserOrder.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertEquals(responseEntity.getBody().getItems().size(), 2);
        assertEquals(responseEntity.getBody().getTotal(), new BigDecimal(200));
        assertEquals(responseEntity.getBody().getUser().getUsername(), "sakshee");
    }

    @Test
    public void getOrdersForUser() {
        ResponseEntity<List> responseEntity = testRestTemplate.getForEntity(url + "/history/sakshee", List.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertEquals(responseEntity.getBody().size(), 1);
    }
}
