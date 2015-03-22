## 线程类型 ##
| **`ThreadType`** | **说明** | **线程名** |
|:-----------------|:-----------|:--------------|
| `vm_thread` | VMThread | `VM Thread` |
| `cgc_thread` | Concurrent GC thread | `Concurrent Mark-Sweep GC Thread` |
| `pgc_thread` | Parallel GC thread | `GC task thread#%d (ParallelGC)` |
| `java_thread` | Java线程 | `-` |
| `compiler_thread` | 编译线程 | `-` |
| `watcher_thread` | 观察线程 | `-` |

线程类的结构图：
```
Thread
|-- NamedThread
|   |-- VMThread
|   |-- ConcurrentGCThread
|   `-- WorkerThread
|       |-- GangWorker
|       `-- GCTaskThread
|-- JavaThread
`-- WatcherThread
```

## 线程信息 ##
  * Java-level thread ID
  * Native thread ID

## HotSpot线程实例说明<sup>[2]</sup> ##
| **线程实例名** | **线程类型** | **说明** |
|:--------------------|:-----------------|:-----------|
| `Attach Listener` | ? | Attach Listener线程是负责接收到外部的命令，而对该命令进行执行的并且吧结果返回给发送者。通常我们会用一些命令去要求jvm给我们一些反馈信息，如：java -version、jmap、jstack等等。如果该线程在jvm启动的时候没有初始化，那么，则会在用户第一次执行jvm命令时，得到启动。 |
| `Signal Dispatcher` | Native | 前面我们提到第一个Attach Listener线程的职责是接收外部jvm命令，当命令接收成功后，会交给signal dispather线程去进行分发到各个不同的模块处理命令，并且返回处理结果。signal dispather线程也是在第一次接收外部jvm命令时，进行初始化工作。 |
| `CompilerThread0` | ? | 用来调用JITing，实时编译装卸class。通常，jvm会启动多个线程来处理这部分工作，线程名称后面的数字也会累加，例如：CompilerThread1 |
| `Concurrent Mark-Sweep GC Thread` | Native | 并发标记清除垃圾回收器（就是通常所说的CMS GC）线程，该线程主要针对于老年代垃圾回收。ps：启用该垃圾回收器，需要在jvm启动参数中加上：-XX:+UseConcMarkSweepGC |
| `DestroyJavaVM ` | Native | 执行main()的线程在main执行完后调用JNI中的jni\_DestroyJavaVM()方法唤起DestroyJavaVM线程。   JVM在Jboss服务器启动之后，就会唤起DestroyJavaVM线程，处于等待状态，等待其它线程（java线程和native线程）退出时通知它卸载JVM。线程退出时，都会判断自己当前是否是整个JVM中最后一个非deamon线程，如果是，则通知DestroyJavaVM线程卸载JVM。<br>ps：<br>扩展一下：<br>1.如果线程退出时判断自己不为最后一个非deamon线程，那么调用thread->exit(false)，并在其中抛出thread_end事件，jvm不退出。<br>2.如果线程退出时判断自己为最后一个非deamon线程，那么调用before_exit()方法，抛出两个事件： 事件1：thread_end线程结束事件、事件2：VM的death事件。<br>然后调用thread->exit(true)方法，接下来把线程从active list卸下，删除线程等等一系列工作执行完成后，则通知正在等待的DestroyJavaVM线程执行卸载JVM操作。 <br>
<tr><td> <code>Finalizer</code> </td><td> Java </td><td> 这个线程也是在main线程之后创建的，其优先级为10，主要用于在垃圾收集前，调用对象的finalize()方法；关于Finalizer线程的几点：<br>1)只有当开始一轮垃圾收集时，才会开始调用finalize()方法；因此并不是所有对象的finalize()方法都会被执行；<br>2)该线程也是daemon线程，因此如果虚拟机中没有其他非daemon线程，不管该线程有没有执行完finalize()方法，JVM也会退出；<br>3) JVM在垃圾收集时会将失去引用的对象包装成Finalizer对象（Reference的实现），并放入ReferenceQueue，由Finalizer线程来处理；最后将该Finalizer对象的引用置为null，由垃圾收集器来回收；<br>4) JVM为什么要单独用一个线程来执行finalize()方法呢？如果JVM的垃圾收集线程自己来做，很有可能由于在finalize()方法中误操作导致GC线程停止或不可控，这对GC线程来说是一种灾难； </td></tr>
<tr><td> <code>Gang worker#0</code> </td><td> Native </td><td> JVM用于做新生代垃圾回收（monir gc）的一个线程。#号后面是线程编号，例如：Gang worker#1 </td></tr>
<tr><td> <code>GC Daemon</code> </td><td> ? </td><td> GC Daemon线程是JVM为RMI提供远程分布式GC使用的，GC Daemon线程里面会主动调用System.gc()方法，对服务器进行Full GC。 其初衷是当RMI服务器返回一个对象到其客户机（远程方法的调用方）时，其跟踪远程对象在客户机中的使用。当再没有更多的对客户机上远程对象的引用时，或者如果引用的“租借”过期并且没有更新，服务器将垃圾回收远程对象。<br>不过，我们现在jvm启动参数都加上了-XX:+DisableExplicitGC配置，所以，这个线程只有打酱油的份了。 </td></tr>
<tr><td> <code>Java2D Disposer</code> </td><td> ? </td><td> 这个线程主要服务于awt的各个组件。说起该线程的主要工作职责前，需要先介绍一下Disposer类是干嘛的。Disposer提供一个addRecord方法。如果你想在一个对象被销毁前再做一些善后工作，那么，你可以调用Disposer#addRecord方法，将这个对象和一个自定义的DisposerRecord接口实现类，一起传入进去，进行注册。  <br>Disposer类会唤起“Java2D Disposer”线程，该线程会扫描已注册的这些对象是否要被回收了，如果是，则调用该对象对应的DisposerRecord实现类里面的dispose方法。<br>Disposer实际上不限于在awt应用场景，只是awt里面的很多组件需要访问很多操作系统资源，所以，这些组件在被回收时，需要先释放这些资源。</td></tr>
<tr><td> <code>JDWP Event Helper Thread</code> </td><td> Native </td><td> JDWP是通讯交互协议，它定义了调试器和被调试程序之间传递信息的格式。它详细完整地定义了请求命令、回应数据和错误代码，保证了前端和后端的JVMTI和JDI的通信通畅。 该线程主要负责将JDI事件映射成JVMTI信号，以达到调试过程中操作JVM的目的。 </td></tr>
<tr><td> <code>JDWP Transport Listener: dt_socket</code> </td><td> Native </td><td> 该线程是一个Java Debugger的监听器线程，负责受理客户端的debug请求。通常我们习惯将它的监听端口设置为8787。 </td></tr>
<tr><td> <code>Low Memory Detector</code> </td><td> Native </td><td> 这个线程是负责对可使用内存进行检测，如果发现可用内存低，分配新的内存空间。 </td></tr>
<tr><td> <code>process reaper</code> </td><td> ? </td><td> 该线程负责去执行一个OS命令行的操作。 </td></tr>
<tr><td> <code>Reference Handler</code> </td><td> Java </td><td> JVM在创建main线程后就创建Reference Handler线程，其优先级最高，为10，它主要用于处理引用对象本身（软引用、弱引用、虚引用）的垃圾回收问题。 </td></tr>
<tr><td> <code>Surrogate Locker Thread (CMS)</code> </td><td> ? </td><td> 这个线程主要用于配合CMS垃圾回收器使用，它是一个守护线程，其主要负责处理GC过程中，Java层的Reference（指软引用、弱引用等等）与jvm内部层面的对象状态同步。这里对它们的实现稍微做一下介绍：这里拿WeakHashMap做例子，将一些关键点先列出来（我们后面会将这些关键点全部串起来）：<br>1. 我们知道HashMap用Entry<a href='.md'>.md</a>数组来存储数据的，WeakHashMap也不例外,内部有一个Entry<a href='.md'>.md</a>数组。<br>2. WeakHashMap的Entry比较特殊，它的继承体系结构为Entry->WeakReference->Reference。<br>3. Reference里面有一个全局锁对象：Lock，它也被称为pending_lock.   注意：它是静态对象。<br>4. Reference 里面有一个静态变量：pending。<br>5. Reference 里面有一个静态内部类：ReferenceHandler的线程，它在static块里面被初始化并且启动，启动完成后处于wait状态，它在一个Lock同步锁模块中等待。<br>6. 另外，WeakHashMap里面还实例化了一个ReferenceQueue列队，这个列队的作用，后面会提到。<br>7. 上面关键点就介绍完毕了，下面我们把他们串起来。<br>假设，WeakHashMap对象里面已经保存了很多对象的引用。JVM在进行CMS GC的时候，会创建一个ConcurrentMarkSweepThread（简称CMST）线程去进行GC，ConcurrentMarkSweepThread线程被创建的同时会创建一个SurrogateLockerThread（简称SLT）线程并且启动它，SLT启动之后，处于等待阶段。CMST开始GC时，会发一个消息给SLT让它去获取Java层Reference对象的全局锁：Lock。直到CMS GC完毕之后，JVM会将WeakHashMap中所有被回收的对象所属的WeakReference容器对象放入到Reference的pending属性当中（每次GC完毕之后，pending属性基本上都不会为null了），然后通知SLT释放并且notify全局锁:Lock。此时激活了ReferenceHandler线程的run方法，使其脱离wait状态，开始工作了。ReferenceHandler这个线程会将pending中的所有WeakReference对象都移动到它们各自的列队当中，比如当前这个WeakReference属于某个WeakHashMap对象，那么它就会被放入相应的ReferenceQueue列队里面（该列队是链表结构）。当我们下次从WeakHashMap对象里面get、put数据或者调用size方法的时候，WeakHashMap就会将ReferenceQueue列队中的WeakReference依依poll出来去和Entry<a href='.md'>.md</a>数据做比较，如果发现相同的，则说明这个Entry所保存的对象已经被GC掉了，那么将Entry<a href='.md'>.md</a>内的Entry对象剔除掉。 </td></tr>
<tr><td> <code>taskObjectTimerFactory</code> </td><td> ? </td><td> 顾名思义，该线程就是用来执行任务的。当我们把一个认为交给Timer对象，并且告诉它执行时间，周期时间后，Timer就会将该任务放入任务列队，并且通知taskObjectTimerFactory线程去处理任务，taskObjectTimerFactory线程会将状态为取消的任务从任务列队中移除，如果任务是非重复执行类型的，则在执行完该任务后，将它从任务列队中移除，如果该任务是需要重复执行的，则计算出它下一次执行的时间点。 </td></tr>
<tr><td> <code>VM Periodic Task Thread</code> </td><td> Native </td><td> 该线程是JVM周期性任务调度的线程，它由WatcherThread创建，是一个单例对象。该线程在JVM内使用得比较频繁，比如：定期的内存监控、JVM运行状况监控，还有我们经常需要去执行一些jstat这类命令查看gc的情况，如下：<br>jstat -gcutil 23483 250 7  这个命令告诉jvm在控制台打印PID为：23483的gc情况，间隔250毫秒打印一次，一共打印7次。 </td></tr>
<tr><td> <code>VM Thread</code> </td><td> Native </td><td> 这个线程就比较牛b了，是jvm里面的线程母体，根据hotspot源码（vmThread.hpp）里面的注释，它是一个单例的对象（最原始的线程）会产生或触发所有其他的线程，这个单个的VM线程是会被其他线程所使用来做一些VM操作（如，清扫垃圾等）。<br>在 VMThread的结构体里有一个VMOperationQueue列队，所有的VM线程操作(vm_operation)都会被保存到这个列队当中，VMThread本身就是一个线程，它的线程负责执行一个自轮询的loop函数(具体可以参考：VMThread.cpp里面的void VMThread::loop())，该loop函数从VMOperationQueue列队中按照优先级取出当前需要执行的操作对象(VM_Operation)，并且调用VM_Operation->evaluate函数去执行该操作类型本身的业务逻辑。<br>ps：VM操作类型被定义在vm_operations.hpp文件内，列举几个：ThreadStop、ThreadDump、PrintThreads、GenCollectFull、GenCollectFullConcurrent、CMS_Initial_Mark、CMS_Final_Remark…..有兴趣的同学，可以自己去查看源文件。 </td></tr></tbody></table>

<h3>参考资料</h3>
<code>[1].</code> <a href='https://gist.github.com/843622/'>https://gist.github.com/843622/</a><br>
<code>[2].</code> <a href='http://w19995.blog.51cto.com/6194463/1052618'>http://w19995.blog.51cto.com/6194463/1052618</a><br>