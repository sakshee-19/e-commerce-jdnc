package com.example.demo.controllers;


import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.security.UserDetailsServiceImpl;
import com.example.demo.utils.Data;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CartController.class)
public class CartControllerTest {
    private String url;

    private Data data;

    List<Item> items;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CartRepository cartRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;

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

        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(items.get(0)));
        when(itemRepository.findByName("nam0")).thenReturn(items);

        when(cartRepository.save(Mockito.any())).thenReturn(cart);
    }

    @Test
    public void removeFromCartTest() throws Exception {
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = Obj.writeValueAsString(modifyCartRequest());
        System.out.println(jsonStr);
        mockMvc.perform(post("/api/cart/removeFromCart")
                .with(SecurityMockMvcRequestPostProcessors.user("sakshee"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"id\":1,\"items\":[{\"id\":0,\"name\":\"nam1\",\"price\":100,\"description\":\"name 1\"}],\"user\":{\"id\":0,\"username\":\"sakshee\"},\"total\":-100}"));
        verify(itemRepository, times(1)).findById(Mockito.any());
        verify(cartRepository, times(1)).save(Mockito.any());
        verify(userRepository, times(1)).findByUsername("sakshee");
    }

    @Test
    public void addToCartReq() throws Exception {
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = Obj.writeValueAsString(modifyCartRequest());
        System.out.println(jsonStr);
        mockMvc.perform(post("/api/cart/addToCart")
                .with(SecurityMockMvcRequestPostProcessors.user("sakshee"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"id\":1,\"items\":[{\"id\":1,\"name\":\"nam0\",\"price\":100,\"description\":\"name 0\"},{\"id\":0,\"name\":\"nam1\",\"price\":100,\"description\":\"name 1\"},{\"id\":1,\"name\":\"nam0\",\"price\":100,\"description\":\"name 0\"},{\"id\":1,\"name\":\"nam0\",\"price\":100,\"description\":\"name 0\"},{\"id\":1,\"name\":\"nam0\",\"price\":100,\"description\":\"name 0\"}],\"user\":{\"id\":0,\"username\":\"sakshee\"},\"total\":500}"));
        verify(itemRepository, times(1)).findById(Mockito.any());
        verify(cartRepository, times(1)).save(Mockito.any());
        verify(userRepository, times(1)).findByUsername("sakshee");

    }
}
