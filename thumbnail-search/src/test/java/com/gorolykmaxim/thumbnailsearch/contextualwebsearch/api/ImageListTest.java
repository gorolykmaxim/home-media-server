package com.gorolykmaxim.thumbnailsearch.contextualwebsearch.api;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class ImageListTest {
    @Test
    public void images() {
        List<Image> images = Collections.singletonList(new Image());
        ImageList imageList = new ImageList();
        imageList.setImages(images);
        Assert.assertEquals(images, imageList.getImages());
    }

    @Test
    public void twoListsAreSame() {
        ImageList imageList1 = new ImageList();
        imageList1.setImages(Collections.emptyList());
        ImageList imageList2 = new ImageList();
        imageList2.setImages(Collections.emptyList());
        Assert.assertEquals(imageList1, imageList2);
        Assert.assertEquals(imageList1.hashCode(), imageList2.hashCode());
    }

    @Test
    public void twoListsAreDifferent() {
        ImageList imageList1 = new ImageList();
        imageList1.setImages(Collections.emptyList());
        ImageList imageList2 = new ImageList();
        imageList2.setImages(Collections.singletonList(new Image()));
        Assert.assertNotEquals(imageList1, imageList2);
        Assert.assertNotEquals(imageList1.hashCode(), imageList2.hashCode());
    }
}