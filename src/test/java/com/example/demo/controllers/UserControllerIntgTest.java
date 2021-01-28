package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
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

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntgTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String url;

    private Data data;

    User user;

    @MockBean
    UserRepository userRepository;

    @Before
    public void setUp() {
        url = "http://localhost:" + port + "/api/user";
        data = new Data();
        user = data.crateUser("sakshee", 1L);
        when(userRepository.findByUsername("sakshee")).thenReturn(user);
        when(userRepository.save(Mockito.any())).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));

    }

    @Test
    public void findUserById() {
        ResponseEntity<User> responseEntity = testRestTemplate.getForEntity(url + "/id/1", User.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertEquals(responseEntity.getBody().getId(), 0);
        assertEquals(responseEntity.getBody().getUsername(), "sakshee");
    }


    @Test
    public void findUserByUsername() {
        ResponseEntity<User> responseEntity = testRestTemplate.getForEntity(url + "/sakshee", User.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertEquals(responseEntity.getBody().getId(), 0);
        assertEquals(responseEntity.getBody().getUsername(), "sakshee");
    }

    public CreateUserRequest createUserRequest() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("sakshee");
        return request;
    }

    @Test
    public void createUser() {
        ResponseEntity<User> responseEntity = testRestTemplate.postForEntity(url + "/create", createUserRequest(), User.class);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.OK));
        assertEquals(responseEntity.getBody().getId(), 0);
        assertEquals(responseEntity.getBody().getUsername(), "sakshee");
    }


}
