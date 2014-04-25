package me.hatter.tools.commons.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CollectionUtilTest {

    @Test
    public void testSplit() {
        Assert.assertEquals(new ArrayList<List<?>>(), CollectionUtil.split(null, 1, false));
        Assert.assertEquals(Arrays.asList(Arrays.<String> asList("1"), Arrays.<String> asList("2")),
                            CollectionUtil.split(Arrays.<String> asList("1", "2"), 1, false));
        Assert.assertEquals(Arrays.asList(Arrays.<String> asList("1", "2")),
                            CollectionUtil.split(Arrays.<String> asList("1", "2"), 2, false));
        Assert.assertEquals(Arrays.asList(Arrays.<String> asList("1", "2"), Arrays.<String> asList("3")),
                            CollectionUtil.split(Arrays.<String> asList("1", "2", "3"), 2, false));
        Assert.assertEquals(Arrays.asList(Arrays.<String> asList("1", "2"), Arrays.<String> asList(null, "3")),
                            CollectionUtil.split(Arrays.<String> asList("1", "2", null, "3"), 2, false));
        Assert.assertEquals(Arrays.asList(Arrays.<String> asList("1", "2"), Arrays.<String> asList("3")),
                            CollectionUtil.split(Arrays.<String> asList("1", "2", null, "3"), 2, true));
        Assert.assertEquals(Arrays.asList(Arrays.<String> asList("1", "2"), Arrays.<String> asList("3", "4"),
                                          Arrays.<String> asList("5")),
                            CollectionUtil.split(Arrays.<String> asList("1", "2", null, "3", "4", "5"), 2, true));
    }
}
