package com.example.demo;

import org.junit.Test;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@EnableJpaRepositories
@TestConfiguration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.example.demo")
public class SareetaApplicationTests {

    @Test
    public void contextLoads() {
    }

}
