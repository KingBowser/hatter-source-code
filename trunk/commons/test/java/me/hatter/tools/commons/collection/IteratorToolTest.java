package me.hatter.tools.commons.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class IteratorToolTest {

    @Test
    public void testHead() {
        Assert.assertEquals(0, CollectionUtil.it(new ArrayList<Integer>()).head(1).list().size());
        List<Integer> list = Arrays.asList(1, 2);
        Assert.assertEquals(Arrays.asList(1), CollectionUtil.it(list).head(1).list());
        Assert.assertEquals(Arrays.asList(1, 2), CollectionUtil.it(list).head(2).list());
        Assert.assertEquals(Arrays.asList(1, 2), CollectionUtil.it(list).head(3).list());
    }

    @Test
    public void testHead2() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Assert.assertEquals(Arrays.asList(1), CollectionUtil.it(list).head(1).list());
        Assert.assertEquals(Arrays.asList(1, 2), CollectionUtil.it(list).head(2).list());
        Assert.assertEquals(Arrays.asList(1, 2, 3), CollectionUtil.it(list).head(3).list());
    }

    @Test
    public void testTail() {
        Assert.assertEquals(0, CollectionUtil.it(new ArrayList<Integer>()).tail(1).list().size());
        List<Integer> list = Arrays.asList(9, 10);
        Assert.assertEquals(Arrays.asList(10), CollectionUtil.it(list).tail(1).list());
        Assert.assertEquals(Arrays.asList(9, 10), CollectionUtil.it(list).tail(2).list());
        Assert.assertEquals(Arrays.asList(9, 10), CollectionUtil.it(list).tail(3).list());
    }

    @Test
    public void testTail2() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Assert.assertEquals(Arrays.asList(10), CollectionUtil.it(list).tail(1).list());
        Assert.assertEquals(Arrays.asList(9, 10), CollectionUtil.it(list).tail(2).list());
        Assert.assertEquals(Arrays.asList(8, 9, 10), CollectionUtil.it(list).tail(3).list());
    }
}
