package com.gorolykmaxim.homemediaapp.service.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HomeMediaAppControllerTest {

    private HomeMediaAppController controller;

    @Before
    public void setUp() throws Exception {
        controller = new HomeMediaAppController();
    }

    @Test
    public void index() {
        Assert.assertEquals("redirect:/torrent", controller.index());
    }
}
