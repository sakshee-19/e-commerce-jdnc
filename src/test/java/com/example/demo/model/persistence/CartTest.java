package com.example.demo.model.persistence;

import com.example.demo.utils.PojoTestUtils;
import org.junit.Test;

public class CartTest {
    @Test
    public void testSuper() {
        PojoTestUtils.validateAccessors(Cart.class);
    }
}
