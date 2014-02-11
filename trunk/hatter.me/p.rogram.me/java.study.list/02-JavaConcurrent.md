Java并发编程

1. Java内存模型是什么？
    * [!](http://www.jcp.org/en/jsr/detail?id=133)
1. `synchronized`的作用是什么？
1. `volatile`的作用是什么？
1. 以下代码是不是线程安全？为什么？如果为count加上volatile修饰是否能够做到线程安全？你觉得该怎么做是线程安全的？
%%% prettify ln=1 rl=1
public class Sample {
__private static int count = 0;
__
__public static void increment() {
____count++;
__}
}
%%%
1. 学会使用`java.uti.concurrent.**`下面的类
    * `java.util.concurrent.locks.ReentrantLock`
    * `java.util.concurrent.locks.ReentrantReadWriteLock`
    * `java.util.concurrent.atomic.Atomic*`
    * `java.util.concurrent.ConcurrentHashMap`
    * `java.util.concurrent.Executors`
    * ...
1. 分析并解释一下下面两段代码的差别：
%%% prettify ln=1 rl=1
// 代码1
public class Sample {
__private static int count = 0;
__
__synchronized public static void increment() {
____count++;
__}
}
__
// 代码2
public class Sample {
__private static AtomicInteger count = new AtomicInteger(0);
__
__public static void increment() {
____count.getAndIncrement();
__}
}
%%%

#### 相关资料
1. [!](http://www.cs.umd.edu/~pugh/java/memoryModel/)
1. [!](http://gee.cs.oswego.edu/dl/jmm/cookbook.html)
1. [!](http://book.douban.com/subject/10484692/)
1. [!](http://www.intel.com/content/www/us/en/processors/architectures-software-developer-manuals.html)


