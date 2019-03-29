package com.gorolykmaxim.homemediaapp.service.view.tvshow;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TvShowPrototypeTest {
    private String name;

    @Before
    public void setUp() throws Exception {
        name = "Doctor Who";
    }

    @Test
    public void name() {
        TvShowPrototype prototype = new TvShowPrototype();
        prototype.setName(name);
        Assert.assertEquals(name, prototype.getName());
    }

    @Test
    public void twoPrototypesAreSame() {
        TvShowPrototype prototype1 = new TvShowPrototype();
        prototype1.setName(name);
        TvShowPrototype prototype2 = new TvShowPrototype();
        prototype2.setName(name);
        Assert.assertEquals(prototype1, prototype2);
        Assert.assertEquals(prototype1.hashCode(), prototype2.hashCode());
    }

    @Test
    public void twoPrototypesAreDifferent() {
        TvShowPrototype prototype1 = new TvShowPrototype();
        prototype1.setName(name);
        TvShowPrototype prototype2 = new TvShowPrototype();
        prototype2.setName("different name");
        Assert.assertNotEquals(prototype1, prototype2);
        Assert.assertNotEquals(prototype1.hashCode(), prototype2.hashCode());
    }
}
