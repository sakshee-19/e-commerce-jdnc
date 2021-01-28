package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

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
