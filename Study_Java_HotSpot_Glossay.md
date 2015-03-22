## HotSpot虚拟机相关的词汇列表 ##
  * **`HotSpot`** <sup>[2][3]</sup>
> HotSpot是较新的Java虚拟机技术，也是官方JDK<sup>注1</sup>自带的Java虚拟机，用来代替JIT<sup>注2</sup>技术，可以大大提高Java运行的性能。 其官方网站为：http://openjdk.java.net/groups/hotspot/。
  * **`JDK`** <sup>[12]</sup>
> Java Development Kit (JDK) 是Sun公司针对Java开发人员发布的免费软件开发工具包(SDK，Software development kit)。自从Java推出以来，JDK已经成为使用最广泛的Java SDK。由于JDK的一部分特性采用商业许可证，而非开源[1](1.md)。因此，2006年Sun公司宣布将发布基于GPL协议的开源JDK，使JDK成为自由软件。在去掉了少量闭源特性之后，Sun公司最终促成了GPL协议的OpenJDK的发布。

---

  * **`adaptive spinning` 自适应自旋** <sup>[1][9]</sup>
> 互斥同步对性能最大的影响是阻塞的实现，挂起线程和恢复线程的操作都需要转入内核态<sup>注4</sup>中完成，这些操作给系统的并发性能带来了很大的压力。同时，虚拟机的开发团队也注意到在许多应用上，共享数据的锁定状态只会持续很短的一段时间，为了这段时间去挂起和恢复线程并不值得。如果物理机器有一个以上的处理器，能让两个或以上的线程同时并行执行，我们就可以让后面请求锁的那个线程“稍等一会”，但不放弃处理器的执行时 间，看看持有锁的线程是否很快就会释放锁。为了让线程等待，我们只须让线程执行一个忙循环（自旋），这项技术就是所谓的自旋锁。 <br>在JDK 1.6中引入了自适应的自旋锁。自适应意味着自旋的时间不再固定了，而是由前一次在同一个锁上的自旋时间及锁的拥有者的状态来决定。如果在同一个锁对象上，自旋等待刚刚成功获得过锁，并且持有锁的线程正在运行中，那么虚拟机就会认为这次自旋也很有可能再次成功，进而它将允许自旋等待持续相对更长的时间， 比如100个循环。另一方面，如果对于某个锁，自旋很少成功获得过，那在以后要获取这个锁时将可能省略掉自旋过程，以避免浪费处理器资源。有了自适应自旋，随着程序运行和性能监控信息的不断完善，虚拟机对程序锁的状况预测就会越来越准确，虚拟机就会变得越来越“聪明”了。<br>
</li></ul><ul><li><b><code>biased locking</code> 偏向锁</b> <sup>[1][6][7]</sup><br>
</li></ul><blockquote>Java偏向锁(`Biased Locking`)是Java6引入的一项多线程优化。它通过消除资源无竞争情况下的同步原语，进一步提高了程序的运行性能。偏向锁，顾名思义，它会偏向于第一个访问锁的线程，如果在接下来的运行过程中，该锁没有被其他的线程访问，则持有偏向锁的线程将永远不需要触发同步。如果在运行过程中，遇到了其他线程抢占锁，则持有偏向锁的线程会被挂起，JVM会尝试消除它身上的偏向锁，将锁恢复到标准的轻量级锁<sup>注3</sup>。(偏向锁只能在单线程下起作用)。
  * **`block start table` 块起始表** <sup>[1][25]</sup>
> 一个关于堆的一片区域的表格，表格中记录了对象的起始地址（低地址）。
  * **`bootstrap classloader` 启动类装载器** <sup>[1][24]</sup>
> 逻辑上用来装载java 平台核心类的装载器，比较底层，通常都是作为VM的一部分来进行实现，所以从java api层面上，是没法接触到该类装载器。
  * **`bytecode verification` 字节码验证** <sup>[1][24]</sup>
> 在连接阶段对class进行的一种处理，用来分析class里的方法的字节码，以便确认其类型安全型。
  * **`C1 compiler`** <sup>[1][24]</sup>
> 快速，轻量级的字节码编译器。能够执行内联，class分析，value numbering(what is this?)等工作。使用一个简单的基于CFG的SSA来作为”高”IR，一个基于机器的来作为”低“IR，同时还拥有一个线性的寄存器分配扫描器，以及一个基于模板的代码生成器。C1 compiler适用于client machine的场景。
  * **`C2 compiler`** <sup>[1][24]</sup>
> 也被称作”opto”,是经过高度优化的字节码编译器。
  * **`card table` 卡片表** <sup>[1][22]</sup>
> remember set的一种，用于记录代中变化过的普通对象指针（oop），跟踪old-to-yong引用。card table是一个数组，每一个条目对应JVM堆的一块称作card的区域。当工作线程对对象的引用字段的每次更新，执行一次write barrier，把含有引用字段的内存卡片对应的卡片表的条目设置为dirty。
  * **`class data sharing` 类数据共享** <sup>[1][24]</sup>
> 用来记录某些类在内存中的位置，这样这些类可以在后续被直接共享使用，而不再需要从对应的类文件里加载到内存中，这是一个启动优化措施。
  * **`class hierachy analysis` 类层级结构分析(CHA)** <sup>[1][24]</sup>
> 也被称作“CHA”，编译器使用这种分析来确定某些虚函数是否在类层级结构上只有一个实现，如果是这样，这可以使用内联或者其他静态化调用等优化技术来优化这个函数。
  * **`code cache`** <sup>[1][24]</sup>
> 一个用来存放编译过代码的堆，这些对象不会被gc重新定位，但是由于这些代码可能包含oops，所以会被当成gc 的根节点。
  * **`compaction`** <sup>[1][24]</sup>
> 垃圾回收的一种技术，将存活的对象压缩到一个集中的内存区域，从而避免内存碎片。
  * **`concurrency`** <sup>[1][24]</sup>
> 在计算机科学里，一般用来指在逻辑上同时执行多件事情，如果硬件条件成立（比如有多核），则可以实现在物理上同时执行多见事。此时成为parallelism。
  * **`concurrent garbage collection`** <sup>[1][24]</sup>
> 一种特定类型的垃圾收集器，该收集器的特定是其大部分的工作都是和工作线程并发执行的，只有小部分的工作需要stop the world。
  * **`copying garbage collection`** <sup>[1]</sup>
> 一种通过拷贝对象实现的垃圾回收算法。
  * **`deoptimization` 逆优化** <sup>[1][26]</sup>
> deoptimization可以废除经优化后的方法机器码。在stack中如果有一个方法激活（there is an activation of the method on the stack)  。stack frame将被转换为interpreter frame，代码将在interpreter中继续执行。另外，vm将通过在entry point 控制的方式使机器码将不在被执行，而是进入interpreter 。我们可以通过这种方式deoptimaze所有编译过的方法，以确保没有由在错误的假定上生成的机器码在运行。
  * **`dependency`** <sup>[1]</sup>
  * **`eden`** <sup>[1]</sup>
  * **`free list`** <sup>[1]</sup>
  * **`garbage collection`** <sup>[1]</sup>
> 垃圾回收算法，一种对内存的自动管理。
  * **`garbage collection root`** <sup>[1]</sup>
> 从堆外指向堆中的Java对象的指针。这种指针包括：类中的表态成员变量，活动的Frame中的本地引用等。
  * **`GC map`** <sup>[1]</sup>
  * **`generational garbage collection`** <sup>[1]</sup>
  * **`handle`** <sup>[1][24]</sup>
> 对oop的再一次包装，这样jvm底层的c/c++代码都是通过handle来操作oop，而垃圾收集器也通过操作handle，能够更容易找到根引用（root reference）。同时，当进行垃圾回收时（也就是c/c++ 代码处于safepoint时），垃圾收集器可能会对handle里的oop做修改（比如将有些对象进行了压缩，或者从新生代移动到了老生代等），而此时对于那些c/c++代码来说，由于它们操作的handle没有变（只是handle里的oop变了而已），所以它们不用感知到这个变化，等其通过handle去获取里面的oop时，此时才感知到变化。所以handle的功能概括来就是，通过引入一个间接层，屏蔽其他代码（非垃圾回收代码）对于oop变动的感知。Handle既有全局的，也有线程可见的。不同的oop会有其对应的handle，而垃圾回收器需要了解所有的这些handle。
  * **`hot lock`** <sup>[1]</sup>
  * **`interpreter`** <sup>[1]</sup>
  * **`JIT compilers`** <sup>[1]</sup>
  * **`JNI` Java本地调用** <sup>[1][15][16]</sup>
> Java Native Interface(JNI)允许Java程序和其它编程语言（如C/C++）写的程序集成（Java程序和Native程序可以相互调用）。允许程序在不放弃遗留代码的情况下使用Java所有特性。但在使用JNI中一些细微的错误都有可能引起整个JVM带来问题，但这种问题很难调度和再现；仅应用和签名过的Applet可以调用JNI。
  * **`JVM TI`** <sup>[1][17][18]</sup>
> Java Virtual Machine Tools Interface是用于开发和调试Java程序的API标准。 JVM通过JVM TI为外部访问JVM内部状态定义了标准的服务，基本上各种性能剖析工具都是基于JVM TI及其前身JVM DI/JVM PI开发的，包括Visual VM、Eclipse TPTP(Test and Performance Tools Platform)的CPU Profiler和Memory Profiler，JVM TI的前身是JVM PI(JVM Profile Interface)和JVM DI(JVM Debug Interface)，其中JVM PI在JDK6中已经被废弃，但仍然有不少剖析工具是基于JVM PI开发的，譬如JProfiler、TPTP都支持JVM PI。
  * **`klass pointer`** <sup>[1]</sup>
  * **`mark word`** <sup>[1]</sup>
  * **`nmethod`** <sup>[1]</sup>
  * **`object header`** <sup>[1]</sup>
  * **`object promotion`** <sup>[1]</sup>
  * **`old generation`** <sup>[1]</sup>
  * **`on-stack replacement`** <sup>[1]</sup>
  * **`oop` 普通对象指针** <sup>[1][13][24]</sup>
> oop 即 ordinary object pointer，普通对象指针，在HotSpot中管理指向对象的指针，特别的指向GC管理的内存空间中的对象指针。实现方式是机器物理地址，而不是句柄。oop可以被编译或解释的Java字节码直接操纵，因为在这些代码中GC知道对象是否仍然存活和物理位置。oop也能被c/c++代码操作，但是当经历safepoints，这些代码必须通过handle来操作oop。
  * **`parallel classloading`** <sup>[1]</sup>
  * **`parallel garbage collection`** <sup>[1]</sup>
  * **`permanent generation`** <sup>[1]</sup>
  * **`remembered set`** <sup>[1]</sup>
> 用于记录分代之前引用指针的数据结构，如年老代对象引用新生代对象时，即使在新生代已经没有对象引用该对象，通过查询该数据结构，不在新生代GC时释放该对象。
  * **`safepoint`** <sup>[1][14][23]</sup>
> safe point 顾明思意，就是安全点，当需要jvm做一些操作的时候，需要把当前正在运行的线程进入一个安全点的状态（也可以说停止状态），这样才能做一些安全的操作，比如线程的dump，堆栈的信息。<br>
在jvm里面通常vm_thread（我们一直在谈论的做一些属于vm 份内事情的线程） 和cms_thread（内存回收的线程）做的操作，是需要将其他的线程通过调用SafepointSynchronize::begin 和 SafepointSynchronize:end来实现让其他的线程进入或者退出safe point 的状态。<br>
</blockquote>  * **`sea-of-nodes`** <sup>[1]</sup>
  * **`Serviceability Agent(SA)`** <sup>[1][19][20][21]</sup>
> Serviceablity Agent是一组Sun（现在是Oracle）内部的代码，用于调试HotSpot的问题。也被部分JDK工具所使用，如 jstack, jmap, jinfo 和 jdb等。其相关Java代码位于`$JAVA_HOME/lib/sa-jdi.jar`。借助SA我们可以枚举Heap和Perm区的对象，Dump JVM中动态增强过的Class类，甚至可以利用其代码制作反汇编程序。
  * **`stackmap`** <sup>[1]</sup>
  * **`StackMapTable`** <sup>[1]</sup>
  * **`survivor space`** <sup>[1]</sup>
  * **`synchronization`** <sup>[1]</sup>
  * **`TLAB`** <sup>[1]</sup>
  * **`uncommon trap`** <sup>[1]</sup>
  * **`verifier`** <sup>[1]</sup>
  * **`VM Operations`** <sup>[1]</sup>
  * **`write barrier`** <sup>[1]</sup>
  * **`young generation`** <sup>[1]</sup>

<br><br>
<hr />
<br>
<sup>注1.</sup> JDK即Java Development Kit的缩写，即Java开发套件，JDK中包含 java, javac, jps, jstat等工具 <sup>[4]</sup><br>
<sup>注2.</sup> JIT即just-in-time compilation的缩写，即即时编译之意 <sup>[5]</sup><br>
<sup>注3.</sup> 轻量级锁（Lightweight Locking）本意是为了减少多线程进入互斥的几率，并不是要替代互斥。它利用了CPU原语Compare-And-Swap(CAS，汇编指令CMPXCHG)，尝试在进入互斥前，进行补救 <sup>[8]</sup><br>
<sup>注4.</sup> 内核态即内核空间(Kernel space) <sup>[10][11]</sup><br>

<br><br>

<hr />
<h3>参考资料</h3>
<code>[1].</code> <a href='http://openjdk.java.net/groups/hotspot/docs/HotSpotGlossary.html'>http://openjdk.java.net/groups/hotspot/docs/HotSpotGlossary.html</a><br>
<code>[2].</code> <a href='http://zh.wikipedia.org/wiki/HotSpot_(java)'>http://zh.wikipedia.org/wiki/HotSpot_(java)</a><br>
<code>[3].</code> <a href='http://en.wikipedia.org/wiki/HotSpot'>http://en.wikipedia.org/wiki/HotSpot</a><br>
<code>[4].</code> <a href='http://en.wikipedia.org/wiki/Java_Development_Kit'>http://en.wikipedia.org/wiki/Java_Development_Kit</a><br>
<code>[5].</code> <a href='http://en.wikipedia.org/wiki/Just-in-time_compilation'>http://en.wikipedia.org/wiki/Just-in-time_compilation</a><br>
<code>[6].</code> <a href='https://blogs.oracle.com/dave/entry/biased_locking_in_hotspot'>https://blogs.oracle.com/dave/entry/biased_locking_in_hotspot</a><br>
<code>[7].</code> <a href='http://kenwublog.com/theory-of-java-biased-locking'>http://kenwublog.com/theory-of-java-biased-locking</a><br>
<code>[8].</code> <a href='http://kenwublog.com/theory-of-lightweight-locking-upon-cas'>http://kenwublog.com/theory-of-lightweight-locking-upon-cas</a><br>
<code>[9].</code> <a href='http://zhangyiqian.iteye.com/blog/1188964'>http://zhangyiqian.iteye.com/blog/1188964</a><br>
<code>[10].</code> <a href='http://zh.wikipedia.org/wiki/%E7%B3%BB%E7%BB%9F%E8%B0%83%E7%94%A8'>http://zh.wikipedia.org/wiki/%E7%B3%BB%E7%BB%9F%E8%B0%83%E7%94%A8</a><br>
<code>[11].</code> <a href='http://zh.wikipedia.org/wiki/%E5%86%85%E6%A0%B8%E7%A9%BA%E9%97%B4'>http://zh.wikipedia.org/wiki/%E5%86%85%E6%A0%B8%E7%A9%BA%E9%97%B4</a><br>
<code>[12].</code> <a href='http://zh.wikipedia.org/wiki/JDK'>http://zh.wikipedia.org/wiki/JDK</a><br>
<code>[13].</code> <a href='https://wikis.oracle.com/display/HotSpotInternals/CompressedOops'>https://wikis.oracle.com/display/HotSpotInternals/CompressedOops</a><br>
<code>[14].</code> <a href='http://rednaxelafx.iteye.com/blog/1044951'>http://rednaxelafx.iteye.com/blog/1044951</a><br>
<code>[15].</code> <a href='http://java.sun.com/docs/books/jni/'>http://java.sun.com/docs/books/jni/</a><br>
<code>[16].</code> <a href='http://en.wikipedia.org/wiki/Java_Native_Interface'>http://en.wikipedia.org/wiki/Java_Native_Interface</a><br>
<code>[17].</code> <a href='http://ayufox.iteye.com/blog/654650'>http://ayufox.iteye.com/blog/654650</a><br>
<code>[18].</code> <a href='http://docs.oracle.com/javase/6/docs/platform/jvmti/jvmti.html'>http://docs.oracle.com/javase/6/docs/platform/jvmti/jvmti.html</a><br>
<code>[19].</code> <a href='http://rednaxelafx.iteye.com/blog/730461'>http://rednaxelafx.iteye.com/blog/730461</a><br>
<code>[20].</code> <a href='http://rednaxelafx.iteye.com/blog/729214'>http://rednaxelafx.iteye.com/blog/729214</a><br>
<code>[21].</code> <a href='http://rednaxelafx.iteye.com/blog/727938'>http://rednaxelafx.iteye.com/blog/727938</a><br>
<code>[22].</code> <a href='http://blog.csdn.net/arkblue/article/details/6154488'>http://blog.csdn.net/arkblue/article/details/6154488</a><br>
<code>[23].</code> <a href='http://blog.csdn.net/raintungli/article/details/7162468'>http://blog.csdn.net/raintungli/article/details/7162468</a><br>
<code>[24].</code> <a href='http://blog.hummingbird-one.com/?p=10076'>http://blog.hummingbird-one.com/?p=10076</a><br>
<code>[25].</code> <a href='http://blog.sina.com.cn/s/blog_693e438c0100qoos.html'>http://blog.sina.com.cn/s/blog_693e438c0100qoos.html</a><br>
<code>[26].</code> <a href='http://www.cnblogs.com/redcreen/archive/2011/06/14/2080718.html'>http://www.cnblogs.com/redcreen/archive/2011/06/14/2080718.html</a><br>
