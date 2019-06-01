package com.gorolykmaxim.torrentui.common.keyvalue;

import org.junit.Assert;
import org.junit.Test;

public class KeyValueTest {

    @Test
    public void create() {
        KeyValue keyValue = new KeyValue("key", "value");
        Assert.assertEquals("key", keyValue.getKey());
        Assert.assertEquals("value", keyValue.getValue());
    }

    @Test
    public void twoKeyValuesAreSame() {
        KeyValue keyValue1 = new KeyValue("key", "value");
        KeyValue keyValue2 = new KeyValue("key", "value");
        Assert.assertEquals(keyValue1, keyValue2);
        Assert.assertEquals(keyValue1.hashCode(), keyValue2.hashCode());
    }

    @Test
    public void twoKeyValuesAreDifferent() {
        KeyValue keyValue1 = new KeyValue("key", "value1");
        KeyValue keyValue2 = new KeyValue("key", "value2");
        Assert.assertNotEquals(keyValue1, keyValue2);
        Assert.assertNotEquals(keyValue1.hashCode(), keyValue2.hashCode());
        keyValue2 = new KeyValue("key1", "value1");
        Assert.assertNotEquals(keyValue1, keyValue2);
        Assert.assertNotEquals(keyValue1.hashCode(), keyValue2.hashCode());
    }

}
