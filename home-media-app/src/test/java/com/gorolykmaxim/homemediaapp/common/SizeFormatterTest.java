package com.gorolykmaxim.homemediaapp.common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SizeFormatterTest {

    private SizeFormatter formatter;

    @Before
    public void setUp() throws Exception {
        formatter = new SizeFormatter();
    }

    @Test
    public void format() {
        String formattedSize = formatter.format(1234000);
        Assert.assertEquals("1 MB", formattedSize);
    }
}
