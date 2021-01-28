package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CartRepository cartRepository;

    Data data = new Data();

    User user;

    @Before
    public void setUp() {
        user = data.crateUser("sakshee", 1L);
        when(userRepository.findByUsername("sakshee")).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
    }

    @Test
    public void findByIdTest() throws Exception {
        mockMvc.perform(get("/api/user/id/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"id\":0,\"username\":\"sakshee\"}"));
        verify(userRepository, times(1)).findById((long) 1);
    }

    @Test
    public void findByUsernameTest() throws Exception {
        mockMvc.perform(get("/api/user/sakshee"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json("{\"id\":0,\"username\":\"sakshee\"}"));
        verify(userRepository, times(1)).findByUsername("sakshee");
    }
}
