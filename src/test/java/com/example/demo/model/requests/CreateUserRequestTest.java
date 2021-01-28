package com.example.demo.model.requests;

import com.example.demo.utils.PojoTestUtils;
import org.junit.Test;

public class CreateUserRequestTest {
    @Test
    public void testSuper() {
        PojoTestUtils.validateAccessors(CreateUserRequest.class);
    }
}
