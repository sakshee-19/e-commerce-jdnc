package com.example.demo.model.persistence;

import com.example.demo.utils.PojoTestUtils;
import org.junit.Test;

public class UserOrderTest {
    @Test
    public void testSuper() {
        PojoTestUtils.validateAccessors(UserOrder.class);
    }
}
