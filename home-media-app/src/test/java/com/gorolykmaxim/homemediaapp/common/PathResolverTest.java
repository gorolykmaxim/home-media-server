package com.gorolykmaxim.homemediaapp.common;

import org.junit.Assert;
import org.junit.Test;

public class PathResolverTest {
    @Test
    public void resolve() {
        PathResolver resolver = new PathResolver("/tmp/data");
        String absolutePath = resolver.resolve("new-show");
        Assert.assertEquals("/tmp/data/new-show", absolutePath);
    }
}
