# XStream非静态化实例的性能问题分析 #

我们程序中有每次都new 一个`JaxbXstreamWrapper`实例：
```
    public String getXmlString() {

        JaxbXstreamWrapper jx = new JaxbXstreamWrapper();
        return jx.getFromObject(this);
    }
```


实现这货就是XStream的包装！

通过简单的循环测试，我们可以看到FGC的变化：

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/blog/new_xstream_old_gc.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/blog/new_xstream_old_gc.png)

这个GC的效果相当明显。

通过“classmexer.jar”计算该实例大小，第一次计算出来是“1,163,232”，好家伙这个足足有1M多，这个初始始化当然慢啦！但每次实例化都要1M多么？显然不是，那么每次实例化会有多大呢？

我们知道在JVM中默认的hashCode函数返回的是对象的地址，从`java.lang.Object`的这个描述可以看到“(This is typically implemented by converting the internal address of the object into an integer, but this implementation  technique is not required by the Java<font size='-2'><sup>TM</sup></font> programming language.)”，那么对于重载过hashCode方法的类似如何得到这个地址呢？虽然我们知道在C#中这个是可以做到的，但在Java中这个有点困难，但Java提供了另外一个方法，就是“`System`中的 `public static native int identityHashCode(Object x)`”。

OK，现在修改classmexer的实现，通过对象的地址来判断是不是同一个对象：
```
private static Set<Integer> CACULATED_OBJECTS = new HashSet<Integer>();
if (!CACULATED_OBJECTS.contains(addr)) {
    CACULATED_OBJECTS.add(addr);
    long sz = instrumentation.getObjectSize(o); // 只计算未计算过大小的对象的大小
    total += sz;
}
```


计算多次后得到：
```
1,163,232
41,832
11,776
11,776
11,776
```

根据这个结果我们可以判断每次`new Xstream`会增加11K左右的内存。

但其实我们知道XStream是线程安全的，所以jx对象只要变成static成员就OK了。

这时候再次对40万数据进行测试，发现只要5秒左右就结束了，根本没等我来的及打印GC信息。