package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.security.JWTAuthenticationVerficationFilter;
import com.example.demo.security.UserDetailsServiceImpl;
import com.example.demo.utils.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

//    @MockBean
//    private JWTAuthenticationVerficationFilter jwtAuthenticationVerficationFilter;

    Data data = new Data();

    List<Item> items;

    @Before
    public void setUp() {
        items = data.createItemList(2, null);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(items.get(0)));
        when(itemRepository.findByName("object1")).thenReturn(items);
        when(itemRepository.findAll()).thenReturn(items);
    }

    @Test
    public void findByIdTest() throws Exception {
        mockMvc.perform(get("/api/item/1")
                .with(SecurityMockMvcRequestPostProcessors.user("sakshee")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"id\":0,\"name\":\"nam0\",\"price\":100,\"description\":\"name 0\"}"));
        verify(itemRepository, times(1)).findById((long) 1);
    }

    @Test
    public void findByNameTest() throws Exception {
        mockMvc.perform(get("/api/item/name/object1")
                .with(SecurityMockMvcRequestPostProcessors.user("sakshee")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("[{\"id\":0,\"name\":\"nam0\",\"price\":100,\"description\":\"name 0\"},{\"id\":0,\"name\":\"nam1\",\"price\":100,\"description\":\"name 1\"}]"));
        verify(itemRepository, times(1)).findByName("object1");
    }

    @Test
    public void findAllItems() throws Exception {
        mockMvc.perform(get("/api/item")
                .with(SecurityMockMvcRequestPostProcessors.user("sakshee")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("[{\"id\":0,\"name\":\"nam0\",\"price\":100,\"description\":\"name 0\"},{\"id\":0,\"name\":\"nam1\",\"price\":100,\"description\":\"name 1\"}]"));
        verify(itemRepository, times(1)).findAll();
    }
}