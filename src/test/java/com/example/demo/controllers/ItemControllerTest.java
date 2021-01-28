package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.utils.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private CartRepository cartRepository;

    Data data = new Data();

    List<Item> items;

    @Before
    public void setUp() {
        items = data.createItemList(2, null);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(items.get(0)));
        when(itemRepository.findByName("object1")).thenReturn(items);
    }

    @Test
    public void findByIdTest() throws Exception {
        mockMvc.perform(get("/api/item/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"id\":0,\"name\":\"nam0\",\"price\":100,\"description\":\"name 0\"}"));
        verify(itemRepository, times(1)).findById((long) 1);
    }

    @Test
    public void findByUsernameTest() throws Exception {
        mockMvc.perform(get("/api/item/name/object1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("[{\"id\":0,\"name\":\"nam0\",\"price\":100,\"description\":\"name 0\"},{\"id\":0,\"name\":\"nam1\",\"price\":100,\"description\":\"name 1\"}]"));
        verify(itemRepository, times(1)).findByName("object1");
    }
}