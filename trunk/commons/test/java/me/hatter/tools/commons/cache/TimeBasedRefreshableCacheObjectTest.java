package me.hatter.tools.commons.cache;

import junit.framework.Assert;
import me.hatter.tools.commons.concurrent.ExecutorUtil;

import org.junit.Test;

public class TimeBasedRefreshableCacheObjectTest {

    @Test
    public void test() {
        TimeBasedRefreshableCacheObject<Long> tbrco = new TimeBasedRefreshableCacheObject<Long>(10) {

            private int    index   = 0;
            private Long[] objects = new Long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L };

            @Override
            protected Long refreshObject() throws Exception {
                return objects[index++];
            }
        };
        Assert.assertEquals(1L, tbrco.getObject().longValue());
        Assert.assertEquals(1L, tbrco.getObject().longValue());
        Assert.assertEquals(1L, tbrco.getObject().longValue());
        Assert.assertEquals(1L, tbrco.getObject().longValue());
        Assert.assertEquals(1L, tbrco.getObject().longValue());
        ExecutorUtil.sleep(11L);
        Assert.assertEquals(2L, tbrco.getObject().longValue());
        Assert.assertEquals(2L, tbrco.getObject().longValue());
        Assert.assertEquals(2L, tbrco.getObject().longValue());
        Assert.assertEquals(2L, tbrco.getObject().longValue());
        Assert.assertEquals(2L, tbrco.getObject().longValue());
    }
}
