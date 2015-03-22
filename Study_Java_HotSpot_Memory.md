![https://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/java/hotspot_memory.png](https://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/java/hotspot_memory.png)

| **方法栈&本地方法栈** | 线程创建时产生,方法执行时生成栈帧 |
|:------------------------------|:--------------------------------------------------|
| **方法区** | 存储类的元数据信息 常量等 |
| **堆** | java代码中所有的new操作 |
| **native Memory(C heap)** | Direct Bytebuffer JNI Compile GC |

![https://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/java/hotspot_heap.png](https://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/java/hotspot_heap.png)

| **Young Generation** | 即图中的Eden + From Space + To Space |
|:---------------------|:-----------------------------------------|
|  **Eden** | 存放新生的对象 |
|  **Survivor Space** | 有两个，存放每次垃圾回收后存活的对象 |
| **Old Generation** | Tenured Generation 即图中的Old Space 主要存放应用程序中生命周期长的存活对象 |

| **Permanent Generation** | 保存虚拟机自己的静态(refective)数据；主要存放加载的Class类级别静态对象如class本身，method，field等等；permanent generation空间不足会引发full GC(详见HotSpot VM GC种类) |
|:-------------------------|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Code Cache** | 用于编译和保存本地代码（native code）的内存；JVM内部处理或优化 |

<br>
<hr />
<br>

<b>想跟踪Java程序中哪里new对象最多，有什么好办法。</b><br>
在最新的Oracle JDK7里自带的Java Flight Recorder正好能满足这种需求。只要看AllocObjectInNewTLAB和AllocObjectOutsideTLAB就能几乎无额外开销得到一个非精准的profile。虽说非精准但很多时候已经足以解决问题。用起来超方便。<br>
<br>
<br>
<h3>参考资料</h3>
<code>[1].</code> <a href='http://www.cnblogs.com/redcreen/archive/2011/05/04/2036387.html'>http://www.cnblogs.com/redcreen/archive/2011/05/04/2036387.html</a><br>