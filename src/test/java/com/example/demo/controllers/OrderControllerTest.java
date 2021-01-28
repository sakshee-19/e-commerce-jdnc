package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.security.UserDetailsServiceImpl;
import com.example.demo.utils.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String url;

    private Data data;

    List<Item> items;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

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
        data = new Data();
        items = data.createItemList(2, null);
        items.get(0).setId(1L);
        user = data.crateUser("sakshee", 1L);
        cart = data.createCart(1L, items, user);
        user.setCart(cart);

        when(userRepository.findByUsername("sakshee")).thenReturn(user);
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
    public void submitTest() throws Exception {
        String jsonOutput = "{\"id\":null,\"items\":[{\"id\":1,\"name\":\"nam0\",\"price\":100,\"description\":\"name 0\"},{\"id\":0,\"name\":\"nam1\",\"price\":100,\"description\":\"name 1\"}],\"user\":{\"id\":0,\"username\":\"sakshee\"},\"total\":200}";
        mockMvc.perform(post("/api/order/submit/sakshee")
                .with(SecurityMockMvcRequestPostProcessors.user("sakshee")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(jsonOutput));
        verify(userRepository, times(1)).findByUsername("sakshee");
        verify(orderRepository, times(1)).save(Mockito.any());
    }

    @Test
    public void getOrdersForUser() throws Exception {
        String jsonOutput = "[{\"id\":null,\"items\":[{\"id\":1,\"name\":\"nam0\",\"price\":100,\"description\":\"name 0\"},{\"id\":0,\"name\":\"nam1\",\"price\":100,\"description\":\"name 1\"}],\"user\":{\"id\":0,\"username\":\"sakshee\"},\"total\":200}]";
        mockMvc.perform(get("/api/order/history/sakshee")
                .with(SecurityMockMvcRequestPostProcessors.user("sakshee")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(jsonOutput));
        verify(userRepository, times(1)).findByUsername("sakshee");
        verify(orderRepository, times(1)).findByUser(user);
    }

}
