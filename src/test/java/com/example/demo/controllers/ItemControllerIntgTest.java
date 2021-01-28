package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ItemControllerIntgTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String url;

    private Data data;

    List<Item> items;

    @MockBean
    ItemRepository itemRepository;

    @Before
    public void setUp() {
        url = "http://localhost:" + port + "/api/item";
        data = new Data();
        items = data.createItemList(2, null);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(items.get(0)));
        when(itemRepository.findByName("object1")).thenReturn(items);
        when(itemRepository.findAll()).thenReturn(items);
    }

    @Test
    public void findByIdTest() {
        ResponseEntity<Item> responseEntity = testRestTemplate.getForEntity(url + "/1", Item.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertEquals(responseEntity.getBody().getId(), 0);
        assertEquals(responseEntity.getBody().getPrice(), new BigDecimal(100));
        assertEquals(responseEntity.getBody().getDescription(), "name 0");
        assertEquals(responseEntity.getBody().getName(), "nam0");
    }

    @Test
    public void findAllItem() {
        ResponseEntity<List> responseEntity = testRestTemplate.getForEntity(url, List.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertEquals(responseEntity.getBody().size(), 2);

    }


    @Test
    public void findUserByUsername() {
        ResponseEntity<List> responseEntity = testRestTemplate.getForEntity(url + "/name/object1", List.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertEquals(responseEntity.getBody().size(), 2);
    }
}
