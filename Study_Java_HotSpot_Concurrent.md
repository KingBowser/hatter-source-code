现在的计算机的频率已经很难提升，但现在/将来的趋势是多核，如Nvidia的强大的GPU <sup>[8]</sup>，Intel，ARM也在努力提升核心的数量。所以在现代计算机中，并发编程将成为趋势。

# Java内存模型(JMM, Java Memory Model) #

Java内存模型如图：　<sup>[22]</sup><br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/java_memory_model.png' /><br>

根据Java Language Specification中的说明, jvm系统中存在一个主内存(Main Memory或Java Heap Memory)，Java中所有变量都储存在主存中，对于所有线程都是共享的。每条线程都有自己的工作内存(Working Memory)，工作内存中保存的是主存中某些变量的拷贝，线程对所有变量的操作都是在工作内存中进行，线程之间无法相互直接访问，变量传递均需要通过主存完成。 <sup>[18][22]</sup><br>
在JMM中，多线程间共享数据需要内存屏障<br>
内存屏障(Memory barrier)，也被称为 membar 、 memory fence 或 fence instruction，是一类强制CPU或编译器使用对内存有序操作的指令。 <sup>[15]</sup><br>
鉴于CPU和编译器都可能对指令进行重排序，所以我将内存屏障分为两种：<br>
<code>*</code> 编译器级别的：防止编译器对指令进行重排序，例如GCC的asm volatile("" ::: "memory")等等。<br>
<code>*</code> 处理器级别的：防止处理器对指令进行重排序，例如X86的lock，lfence()，sfenec()等等 <sup>[24]</sup><br>
对于在内存屏障的实现参看如下代码：<br>
<pre><code>public class Singleton {<br>
    private volatile static Singleton instance;<br>
<br>
    public static Singleton getInstance() {<br>
        if (instance == null) {<br>
            synchronized (Singleton.class) {<br>
                if (instance == null) {<br>
                    instance = new Singleton();<br>
                }<br>
            }<br>
        }<br>
        return instance;<br>
    }<br>
<br>
    public static void main(String[] args) {<br>
            Singleton.getInstance();<br>
    }<br>
}<br>
</code></pre>
编译后，这段代码对instance变量赋值部分代码如下所示：<br>
<pre><code>0x01a3de0f: mov    $0x3375cdb0,%esi   ;...beb0cd75 33<br>
                                        ;   {oop('Singleton')}<br>
0x01a3de14: mov    %eax,0x150(%esi)   ;...89865001 0000<br>
0x01a3de1a: shr    $0x9,%esi          ;...c1ee09<br>
0x01a3de1d: movb   $0x0,0x1104800(%esi)  ;...c6860048 100100<br>
0x01a3de24: lock addl $0x0,(%esp)     ;...f0830424 00<br>
                                        ;*putstatic instance<br>
                                        ; - Singleton::getInstance@24<br>
</code></pre>
通过对比发现，关键变化在于有volatile修饰的变量，赋值后（前面mov %eax,0x150(%esi)这句便是赋值操作）多执行了一个“lock addl $0x0,(%esp)”操作，这个操作相当于一个内存屏障，只有一个CPU访问内存时，并不需要内存屏障；但如果有两个或更多CPU访问同一块内存，且其中有一个在观测另一个，就需要内存屏障来保证一致性了。<br>
指令“addl $0x0,(%esp)”显然是一个空操作，关键在于lock前缀，查询IA32手册，它的作用是使得本CPU的Cache写入了内存，该写入动作也会引起别的CPU invalidate其Cache。所以通过这样一个空操作，可让前面volatile变量的修改对其他CPU立即可见。<br>
那为何说它禁止指令重排序呢？从硬件架构上讲，指令重排序是指CPU采用了允许将多条指令不按程序规定的顺序分开发送给各相应电路单元处理。但并不是说指令任意重排，CPU需要能正确处理指令依赖情况保障程序能得出正确的执行结果。譬如指令1把地址A中的值加10，指令2把地址A中的值乘以2，指令3把地址B中的值减去3，这时指令1和指令2是有依赖的，它们之间的顺序不能重排——<code>(A+10）*2</code>与<code>A*2+10</code>显然不相等，但指令3可以重排到指令1、2之前或者中间，只要保证CPU执行后面依赖到A、B值的操作时能获取到正确的A和B值即可。所以在本内CPU中，重排序看起来依然是有序的。因此，lock addl $0x0,(%esp)指令把修改同步到内存时，所有之前的操作都已经执行完成，这样便形成了“指令重排序无法越过内存屏障”的效果。 <sup>[1]</sup><br>
<br>
<h1>伪共享(False Sharing)</h1>

我更喜欢称之为“错误共享”，CPU(Intel 4 core)结构Sample： <sup>[16]</sup><br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/intel_4_core.png' /><br><br>
内存在缓存系统中常以缓存行(cache line)为单位缓存。缓存行的大小则是2的指数，通常在 32~256 字节范围内，最常见的大小为 64 字节。伪共享则是一个术语，在同时修改相关缓存行的独立变量时，在不知不觉的情况下影响不同线程的性能表现。 <sup>[9]</sup><br>
详细图例如下：<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/false_sharing.png' /><br>

CPU访问缓存及内存的速度比较： <sup>[25]</sup><br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/intel_nehalem.png' /><br>

<h1>缓存一致性协议(MESI)</h1>

在MESI协议中，每个Cache line有4个状态，可用2个bit表示，它们分别是 <sup>[26]</sup>：<br>
<table><thead><th> <b>状态</b> </th><th> <b>描述</b> </th></thead><tbody>
<tr><td> M(Modified) </td><td> 这行数据有效，数据被修改了，和内存中的数据不一致，数据只存在于本Cache中。 </td></tr>
<tr><td> E(Exclusive) </td><td> 这行数据有效，数据和内存中的数据一致，数据只存在于本Cache中。 </td></tr>
<tr><td> S(Shared) </td><td> 这行数据有效，数据和内存中的数据一致，数据存在于很多Cache中。 </td></tr>
<tr><td> I(Invalid) </td><td> 这行数据无效 </td></tr></tbody></table>

状态迁移图：<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/mesi.jpeg' /><br>

<h1>锁(Locking	)</h1>

在Java中通过锁来实现线程安全，Java通过monitorenter与monitorexit这两个控制多线程同步的bytecode原语实现锁，是JVM依赖操作系统互斥(mutex)来实现的，所以在JVM中锁的性能相对较大。互斥是一种会导致线程挂起，并在较短的时间内又需要重新调度回原线程的，是较为消耗资源的操作。 <sup>[13]</sup><br>
<br>
<h2>Java中对锁的优化</h2>

Java对象头结构(Java Object Header) <sup>[10]</sup><br>
<pre><code> [Mark Word             ] 32/64 bits<br>
 [Class Metadata Address] 32/64 bits<br>
 [Array Length          ] 32/64 bits<br>
 对象为两个字(Word)，数组为3个字(增加了一个Array Length)<br>
 <br>
 对象的 Mark Word，同步相关的状态(32 bits)：<br>
 [ bitfields                                   ][ tag bits ]  [ state              ]<br>
 -----------------------------------------------------------------------------------<br>
 [ hash                  (25)][ age (4)][ 0 (1)][ 01    (2)]  [ unlocked           ]<br>
 [ ptr to lock record                      (29)][ 00    (2)]  [ lightweight locked ]<br>
 [ ptr to heavyweight record               (29)][ 10    (2)]  [ inflated           ]<br>
 [                                         (29)][ 11    (2)]  [ marked for GC      ]<br>
 [ thread id (23)][ epoch (2)][ age (4)][ 1 (1)][ 01    (2)]  [ biasable           ]<br>
</code></pre>

通过参数<code>-XX:+UseBiasedLocking</code>判断当前是否使用偏向锁（在JDK6，7中默认打开该参数），同步锁转换图如下： <sup>[17]</sup><br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/synchronization.gif' /><br>

<h3>轻量级锁(Lightweight Locking)</h3>
早期的研究表明，在Java中的大部分锁是不存在竞争的，基于这个研究结果发明了轻量级锁（当发生竞争时则膨胀到重量级锁） <sup>[19]</sup>。轻量级锁本意是为了减少多线程进入互斥的几率，并不是要替代互斥。它利用了CPU原语Compare-And-Swap(CAS，汇编指令CMPXCHG)，尝试在进入互斥前，进行补救。 <sup>[13]</sup><br>
<br>
<h3>偏向锁(Biased Locking)</h3>
IBM位于东京的实验室发现，Java程序中的锁不但竞争很少，而且是不共享的，基于这个发现则发明了偏向锁 <sup>[20]</sup>。偏向锁性能非常好，当正常工作时仅需约2~4个CPU周期。 <sup>[11]</sup><br>
偏向锁取消： <sup>[10]</sup><br>
1. 退回到轻量级锁<br>
2. 等待一个全局Safepoint（无字节码执行）<br>
3. 遍历线程栈，枚举锁信息<br>
4. 更新对象头锁信息<br>

<h3>锁膨胀(Lock Coarsening)</h3>
如果一系列的连续操作都对同一个对象反复加锁和解锁，甚至加锁操作是出现在循环体中的，那即使没有线程竞争，频繁地进行互斥同步操作也会导致不必要的性能损耗。如果虚拟机探测到有这样一串零碎的操作都对同一个对象加锁，将会把加锁同步的范围扩展（膨胀）到整个操作序列的外部，这样只需要加锁一次就可以了。参数<code>-XX:+EliminateLocks</code>。 <sup>[14][21]</sup><br>
锁膨胀的例子如下 <sup>[12]</sup>:<br>
<pre><code>public Point moveNW (Point p) {<br>
  synchronized (p) {<br>
    p.x += 10;<br>
  }<br>
  synchronized (p) {<br>
    p.y += 10;<br>
  }<br>
}<br>
<br>
public Point moveNW (Point p) {<br>
  synchronized (p) {<br>
    p.x += 10;<br>
    p.y += 10;<br>
  }<br>
}<br>
</code></pre>

<h3>锁削除(Lock Elision through Escape Analysis)</h3>
锁削除是指虚拟机即时编译器在运行时，对一些代码上要求同步，但是被检测到不可能存在共享数据竞争的锁进行削除。锁削除的主要判定依据来源于逃逸分析（参数：-XX:+DoEscapeAnalysis）的数据支持，如果判断到一段代码中，在堆上的所有数据都不会逃逸出去被其他线程访问到，那就可以把它们当作栈上数据对待，认为它们是线程私有的，同步加锁自然就无须进行。 <sup>[14][21]</sup><br>
<br>
<h3>嵌套锁削除(Eliminate nested locks of the same object)</h3>
对于嵌套锁同一个对象时，因为我们都是可重入锁，所以里面的锁其实是可以削除的，而嵌套锁削除则是对这种场景进行优化。如代码：<br>
<pre><code>    synchronized (reader) { // 同步对象，持有锁：reader<br>
    if (reader.getCount() &gt; 0) { // 调用同步方法：getCount()，但程序已经持有该锁：reader<br>
       reader.incrementReserveCount(1);<br>
    }<br>
</code></pre>
显然上例中“reader.getCount()”的锁是可以忽略掉的。参数：-XX:+EliminateNestedLocks。<sup>[30]</sup><br>
<br>
<h3>自旋锁与自适应自旋(Spinning Locking & Adaptive Spinning)</h3>
互斥同步对性能最大的影响是阻塞的实现，挂起线程和恢复线程的操作都需要转入内核态中完成，这些操作给系统的并发性能带来了很大的压力。同时，虚拟机的开发团队也注意到在许多应用上，共享数据的锁定状态只会持续很短的一段时间，为了这段时间去挂起和恢复线程并不值得。如果物理机器有一个以上的处理器，能让两个或以上的线程同时并行执行，我们就可以让后面请求锁的那个线程“稍等一会”，但不放弃处理器的执行时 间，看看持有锁的线程是否很快就会释放锁。为了让线程等待，我们只须让线程执行一个忙循环（自旋），这项技术就是所谓的自旋锁。 <br>
在JDK 1.6中引入了自适应的自旋锁。自适应意味着自旋的时间不再固定了，而是由前一次在同一个锁上的自旋时间及锁的拥有者的状态来决定。如果在同一个锁对象上，自旋等待刚刚成功获得过锁，并且持有锁的线程正在运行中，那么虚拟机就会认为这次自旋也很有可能再次成功，进而它将允许自旋等待持续相对更长的时间， 比如100个循环。另一方面，如果对于某个锁，自旋很少成功获得过，那在以后要获取这个锁时将可能省略掉自旋过程，以避免浪费处理器资源。有了自适应自旋，随着程序运行和性能监控信息的不断完善，虚拟机对程序锁的状况预测就会越来越准确，虚拟机就会变得越来越“聪明”了。 <sup>[20]</sup><br>
<br>
<h3>可重入锁(ReentrantLock)</h3>
java.util.concurrent.lock 中的 Lock 框架是锁定的一个抽象，它允许把锁定的实现作为 Java 类，而不是作为语言的特性来实现。这就为 Lock 的多种实现留下了空间，各种实现可能有不同的调度算法、性能特性或者锁定语义。 ReentrantLock 类实现了 Lock ，它拥有与 synchronized 相同的并发性和内存语义，但是添加了类似锁投票、定时锁等候和可中断锁等候的一些特性。此外，它还提供了在激烈争用情况下更佳的性能。（换句话说，当许多线程都想访问共享资源时，JVM 可以花更少的时候来调度线程，把更多时间用在执行线程上。） <sup>[2]</sup><br>
<br>
<h3>排队自旋锁(FIFO Ticket Spinlock)<code>[Linux]</code></h3>
排队自旋锁(FIFO Ticket Spinlock)是 Linux 内核 2.6.25 版本引入的一种新型自旋锁，它通过保存执行线程申请锁的顺序信息解决了传统自旋锁的“不公平”问题。排队自旋锁的代码由 Linux 内核开发者 Nick Piggin 实现，目前只针对 x86 体系结构(包括 IA32 和 x86_64)。 <sup>[23]</sup><br>
<br>
<h1>并发支持</h1>

<h2>JVM</h2>
<code>@Contended</code> Sample: <sup>[28][29]</sup><br>
<pre><code>.<br>
    @Contended<br>
    public static class ContendedTest2 {<br>
        private Object plainField1;<br>
        private Object plainField2;<br>
        private Object plainField3;<br>
        private Object plainField4;<br>
    }<br>
</code></pre>

<h2>JUC(<code>java.util.concurrent</code>)</h2>
JDK自带 rt.jar(或classes.jar)<br>

<h2>Disruptor</h2>
<a href='http://code.google.com/p/disruptor/'>http://code.google.com/p/disruptor/</a><br>
High Performance Inter-Thread Messaging Library<br>


<br><br>

<h3>参考资料</h3>
<code>[1].</code> <a href='http://www.infoq.com/cn/articles/zzm-java-hsdis-jvm'>http://www.infoq.com/cn/articles/zzm-java-hsdis-jvm</a><br>
<code>[2].</code> <a href='http://www.ibm.com/developerworks/cn/java/j-jtp10264/index.html'>http://www.ibm.com/developerworks/cn/java/j-jtp10264/index.html</a><br>
<code>[3].</code> <a href='http://kenwublog.com/illustrate-memory-reordering-in-cpu'>http://kenwublog.com/illustrate-memory-reordering-in-cpu</a><br>
<code>[4].</code> <a href='http://hi.baidu.com/ajf8/blog/item/05889719727b691934fa4178.html'>http://hi.baidu.com/ajf8/blog/item/05889719727b691934fa4178.html</a><br>
<code>[5].</code> <a href='http://www.langyuweb.com/a/wangluozhishi/2012/0326/14674.html'>http://www.langyuweb.com/a/wangluozhishi/2012/0326/14674.html</a><br>
<code>[6].</code> <a href='http://www.infoq.com/cn/articles/cf-java-thread'>http://www.infoq.com/cn/articles/cf-java-thread</a><br>
<code>[7].</code> <a href='http://www.goldendoc.org/2011/05/juc/'>http://www.goldendoc.org/2011/05/juc/</a><br>
<code>[8].</code> <a href='http://cloud.csdn.net/a/20100926/279856.html'>http://cloud.csdn.net/a/20100926/279856.html</a><br>
<code>[9].</code> <a href='http://mechanical-sympathy.blogspot.com/2011/07/false-sharing.html'>http://mechanical-sympathy.blogspot.com/2011/07/false-sharing.html</a><br>
<code>[10].</code> <a href='http://edu.netbeans.org/contrib/slides/java-overview-and-java-se6.pdf'>http://edu.netbeans.org/contrib/slides/java-overview-and-java-se6.pdf</a><br>
<code>[11].</code> <a href='http://www.azulsystems.com/blog/wp-content/uploads/2011/03/2011_WhatDoesJVMDo.pdf'>http://www.azulsystems.com/blog/wp-content/uploads/2011/03/2011_WhatDoesJVMDo.pdf</a><br>
<code>[12].</code> <a href='http://wiki.jvmlangsummit.com/pdf/10_Pampuch_vm_opts.pdf'>http://wiki.jvmlangsummit.com/pdf/10_Pampuch_vm_opts.pdf</a><br>
<code>[13].</code> <a href='http://kenwublog.com/theory-of-lightweight-locking-upon-cas'>http://kenwublog.com/theory-of-lightweight-locking-upon-cas</a><br>
<code>[14].</code> <a href='http://icyfenix.iteye.com/blog/1018932'>http://icyfenix.iteye.com/blog/1018932</a><br>
<code>[15].</code> <a href='http://en.wikipedia.org/wiki/Memory_barrier'>http://en.wikipedia.org/wiki/Memory_barrier</a><br>
<code>[16].</code> <a href='http://jakub.wartak.pl/dl/j2ee/sunJVM-on-intel-multicoreservers.pdf'>http://jakub.wartak.pl/dl/j2ee/sunJVM-on-intel-multicoreservers.pdf</a><br>
<code>[17].</code> <a href='https://wikis.oracle.com/display/HotSpotInternals/Synchronization'>https://wikis.oracle.com/display/HotSpotInternals/Synchronization</a><br>
<code>[18].</code> <a href='http://kenwublog.com/explain-java-memory-model-in-detail'>http://kenwublog.com/explain-java-memory-model-in-detail</a><br>
<code>[19].</code> <a href='http://www.oracle.com/technetwork/java/javase/tech/biasedlocking-oopsla2006-preso-150106.pdf'>http://www.oracle.com/technetwork/java/javase/tech/biasedlocking-oopsla2006-preso-150106.pdf</a><br>
<code>[20].</code> <a href='https://code.google.com/p/hatter-source-code/wiki/Study_Java_HotSpot_Glossay'>https://code.google.com/p/hatter-source-code/wiki/Study_Java_HotSpot_Glossay</a><br>
<code>[21].</code> <a href='http://home.comcast.net/~pjbishop/Dave/MustangSync.pdf'>http://home.comcast.net/~pjbishop/Dave/MustangSync.pdf</a><br>
<code>[22].</code> <a href='http://www.javaol.net/2010/10/java-memory-model/'>http://www.javaol.net/2010/10/java-memory-model/</a><br>
<code>[23].</code> <a href='http://www.ibm.com/developerworks/cn/linux/l-cn-spinlock/index.html'>http://www.ibm.com/developerworks/cn/linux/l-cn-spinlock/index.html</a><br>
<code>[24].</code> <a href='http://www.khotyn.com/2011/12/15/memory_barrier/'>http://www.khotyn.com/2011/12/15/memory_barrier/</a><br>
<code>[25].</code> <a href='http://qconlondon.com/dl/qcon-london-2012/slides/MartinThompson_and_MichaelBarker_LockFreeAlgorithmsForUltimatePerformance.pdf'>http://qconlondon.com/dl/qcon-london-2012/slides/MartinThompson_and_MichaelBarker_LockFreeAlgorithmsForUltimatePerformance.pdf</a><br>
<code>[26].</code> <a href='http://www.tektalk.org/2011/07/11/cache%E4%B8%80%E8%87%B4%E6%80%A7%E5%8D%8F%E8%AE%AE%E4%B8%8Emesi2/'>http://www.tektalk.org/2011/07/11/cache%E4%B8%80%E8%87%B4%E6%80%A7%E5%8D%8F%E8%AE%AE%E4%B8%8Emesi2/</a><br>
<code>[27].</code> <a href='https://blogs.oracle.com/dave/entry/biased_locking_in_hotspot'>https://blogs.oracle.com/dave/entry/biased_locking_in_hotspot</a><br>
<code>[28].</code> <a href='https://blogs.oracle.com/dave/entry/java_contented_annotation_to_help'>https://blogs.oracle.com/dave/entry/java_contented_annotation_to_help</a><br>
<code>[29].</code> <a href='http://mail.openjdk.java.net/pipermail/hotspot-dev/2012-November/007309.html'>http://mail.openjdk.java.net/pipermail/hotspot-dev/2012-November/007309.html</a><br>
<code>[30].</code> <a href='http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7125896'>http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7125896</a><br>