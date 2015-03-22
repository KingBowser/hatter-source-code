# HotSpot问题诊断 #
HotSpot即OpenJDK也是Sun/Oracle官方JDK标准JVM实现。所以我们最常用的JVM也就是HotSpot了，对于能够查找HotSpot相关的问题就相当重要，对于问题我们需要根据现象判断，对现象简单分类如下：

## 一、HotSpot异常退出 ##
这个问题比较棘手，因为通常JVM异常退出是JVM自身的Bug，所以一般需要读懂JVM源代码才能够找到/解决这类Bug。
  * http://bugs.sun.com/

也有非Bug引起的JVM异常退出的问题，例如：
  * [ulimit -t 引起的kill血案](http://blog.yufeng.info/archives/2311)

## 二、JVM加载的类有问题 ##
常常在一个应用里有同一个jar包的不同版本，但我们无法断定当前JVM加载的是哪个版本，这时候可以通过 `classdump` 打印URLClassLoader中加载的所有jar包，并列出他们的顺序。而我们实现的使用场景中常常还会使用类增强的手段，如使用ASM、javasist等，这时候需要分析内存中的字节码，此时也可以使用 `classdump` 将对应的类输出到磁盘文件。
```
$ java -jar classdumpall.jar <PID> -filter org.apache.zookeeper.server.persistence.File*
[INFO] Add system classloader jar url: /usr/java/jdk1.6.0_25/lib/tools.jar
[INFO] Add system classloader jar url: /usr/java/jdk1.6.0_25/lib/sa-jdi.jar
Attaching to process ID 23537, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 20.0-b11
[INFO] Output directory: .
[INFO] Dump class: org.apache.zookeeper.server.persistence.FileTxnSnapLog$PlayBackListener  @ sun.misc.Launcher$AppClassLoader
//                 ^-- 类名                                                                    ^-- 加载的ClassLoader
[INFO]        jar: /home/admin/storm/zookeeper-3.3.5/zookeeper-3.3.5.jar
//                 ^-- 类加载的Jar包
[INFO] Dump class: org.apache.zookeeper.server.persistence.FileTxnLog$PositionInputStream  @ sun.misc.Launcher$AppClassLoader
[INFO]        jar: /home/admin/storm/zookeeper-3.3.5/zookeeper-3.3.5.jar
[INFO] Dump class: org.apache.zookeeper.server.persistence.FileTxnSnapLog  @ sun.misc.Launcher$AppClassLoader
[INFO]        jar: /home/admin/storm/zookeeper-3.3.5/zookeeper-3.3.5.jar
[INFO] Dump class: org.apache.zookeeper.server.persistence.FileTxnLog  @ sun.misc.Launcher$AppClassLoader
[INFO]        jar: /home/admin/storm/zookeeper-3.3.5/zookeeper-3.3.5.jar
[INFO] Dump class: org.apache.zookeeper.server.persistence.FileHeader  @ sun.misc.Launcher$AppClassLoader
[INFO]        jar: /home/admin/storm/zookeeper-3.3.5/zookeeper-3.3.5.jar
[INFO] Dump class: org.apache.zookeeper.server.persistence.FileSnap  @ sun.misc.Launcher$AppClassLoader
[INFO]        jar: /home/admin/storm/zookeeper-3.3.5/zookeeper-3.3.5.jar
[INFO] Dump class: org.apache.zookeeper.server.persistence.FileTxnLog$FileTxnIterator  @ sun.misc.Launcher$AppClassLoader
[INFO]        jar: /home/admin/storm/zookeeper-3.3.5/zookeeper-3.3.5.jar
```
Dump出来的类路径如下：
```
.
`-- org
    `-- apache
        `-- zookeeper
            `-- server
                `-- persistence
                    |-- FileHeader.class
                    |-- FileSnap.class
                    |-- FileTxnLog$FileTxnIterator.class
                    |-- FileTxnLog$PositionInputStream.class
                    |-- FileTxnLog.class
                    |-- FileTxnSnapLog$PlayBackListener.class
                    `-- FileTxnSnapLog.class
```
现在你可以通 `javap` 反编译出字节码：
```
$ javap -p -v FileHeader
Compiled from "FileHeader.java"
public class org.apache.zookeeper.server.persistence.FileHeader extends java.lang.Object implements org.apache.jute.Record
  SourceFile: "FileHeader.java"
  minor version: 0
  major version: 49
  Constant pool:
const #1 = Method	#43.#112;	//  java/lang/Object."<init>":()V
... ...
public org.apache.zookeeper.server.persistence.FileHeader(int, int, long);
  Code:
   Stack=3, Locals=5, Args_size=4
   0:	aload_0
   1:	invokespecial	#1; //Method java/lang/Object."<init>":()V
   4:	aload_0
   5:	iload_1
   6:	putfield	#2; //Field magic:I
   9:	aload_0
   10:	iload_2
   11:	putfield	#3; //Field version:I
   14:	aload_0
   15:	lload_3
   16:	putfield	#4; //Field dbid:J
   19:	return
  LineNumberTable: 
   line 33: 0
   line 34: 4
   line 35: 9
   line 36: 14
   line 37: 19

  LocalVariableTable: 
   Start  Length  Slot  Name   Signature
   0      20      0    this       Lorg/apache/zookeeper/server/persistence/FileHeader;
   0      20      1    magic       I
   0      20      2    version       I
   0      20      3    dbid       J
```
你也可以通过 [JD|Java Decompiler](http://java.decompiler.free.fr/) 查看源代码了。
![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/java/java_decompiler.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/java/java_decompiler.png)

## 三、应用停止处理业务逻辑 ##
这种情况需要查看服务器Load是不是Load过高导致不处理正常业务逻辑，参看 `[应用Load过高]` 或 `[应用占用内存过高]` 。如果Load正常且内存占用也正常则需要通过命令 `jstack` 查看堆栈情况，分析程序未正常工作的原因。此类情况有可能是：
  * 数据库锁，更新/查询/删除数据未提交
    * 大事务不提交，其它更新则需等待锁
  * 程序死锁，不恰当的使用Lock
    * 举例1：Lock顺序导致死锁
```
    public static void main(String[] args) {
        final Lock lock1 = new ReentrantLock();
        final Lock lock2 = new ReentrantLock();

        Thread t1 = new Thread() {

            public void run() {
                lock1.lock();
                try {
                    lock2.lock();
                    try {
                        // code
                    } finally {
                        lock2.unlock();
                    }
                } finally {
                    lock1.unlock();
                }
            }
        };
        Thread t2 = new Thread() {

            public void run() {
                lock2.lock();
                try {
                    lock1.lock();
                    try {
                        // code
                    } finally {
                        lock1.unlock();
                    }
                } finally {
                    lock2.unlock();
                }
            }
        };
        t1.start();
        t2.start();
    }
```
> > 通过`jstack`可以看到：
```
Found one Java-level deadlock:
=============================
"Thread-2":
  waiting for ownable synchronizer 7f31a0208, (a java.util.concurrent.locks.ReentrantLock$NonfairSync),
  which is held by "Thread-1"
"Thread-1":
  waiting for ownable synchronizer 7f31a0238, (a java.util.concurrent.locks.ReentrantLock$NonfairSync),
  which is held by "Thread-2"

Java stack information for the threads listed above:
===================================================
"Thread-2":
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <7f31a0208> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:156)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:811)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:842)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1178)
	at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:186)
	at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:262)
	at me.hatter.tool.a.tests.classes.LockL$2.run(LockL.java:41)
"Thread-1":
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <7f31a0238> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:156)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:811)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:842)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1178)
	at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:186)
	at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:262)
	at me.hatter.tool.a.tests.classes.LockL$1.run(LockL.java:21)

Found 1 deadlock.
```
    * 举例2：共用Executor导致死锁
```
    public static void main(String[] args) {
        final ExecutorService es = Executors.newFixedThreadPool(1);
        es.submit(new Runnable() {

            public void run() {
                System.out.println("AAAAAA");
                Future<?> f = es.submit(new Runnable() {

                    public void run() {
                        System.out.println("BBBBBB");
                    }
                });
                try {
                    f.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("CCCCCC");
            }
        });
        System.out.println("DDDDDD");
    }
```
> > 在这里的线程池被共用，但当线程池为1时，共用该线程池导致无法处理正常业务逻辑。
  * 网络IO操作未返回，如未设置超时时间且当网络异常时有可能出现超时但无法继续执行的情况<br>如：URLConnection在使用时需要同时设置 <code>void setConnectTimeout(int timeout)</code> 和 <code>void setReadTimeout(int timeout)</code>，否则有可能在使用时出现客户端等待数据但网络连接一直保持的情况。使用HttpClient时也需要设置类似的两个参数(生产环境悲剧过，大家要引以为戒)。</li></ul>

<h2>四、应用Load过高 ##
这种情况可以使用 `jtop` 查看，通过对堆栈对CPU使用量排序，查找出问题的Java代码。

Load如何计算： CPU，IO etc.

  * Java线程占用CPU高

> 通过 `jtop` 查找CPU最高的线程
```
$java -jar jtop.jar -thread 3 -stack 8 <PID>
DefaultQuartzScheduler_Worker-8  TID=86  STATE=RUNNABLE  CPU_TIME=2110 (99.90%)  USER_TIME=2110 (99.90%) Allocted: 0
        java.util.WeakHashMap.put(WeakHashMap.java:405)
        org.aspectj.weaver.Dump.registerNode(Dump.java:253)
        org.aspectj.weaver.World.<init>(World.java:150)
        org.aspectj.weaver.reflect.ReflectionWorld.<init>(ReflectionWorld.java:50)
        org.aspectj.weaver.tools.PointcutParser.setClassLoader(PointcutParser.java:221)
        org.aspectj.weaver.tools.PointcutParser.<init>(PointcutParser.java:207)
        org.aspectj.weaver.tools.PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(PointcutParser.java:128)
        org.springframework.aop.aspectj.AspectJExpressionPointcut.<init>(AspectJExpressionPointcut.java:100)

...
```
  * Native线程占用CPU高
> 如果排除Java线程占用CPU高，但JVM占用CPU高时则说明是JVM Native线程占用CPU高了，这时需要通过 `pstack` 或 `gdb -ex "set pagination 0" -ex "thread apply all bt" --batch -p <PID>` 来查找问题了。
```
$ jstack <PID> | grep 'VM Thread'
"VM Thread" prio=10 tid=0x000000004288a000 nid=0x4b4e runnable
//                                             ^-- native thread id = 19278
```
> 再通过 gdb (或 pstack)打印堆栈信息：
```
$ gdb -ex "set pagination 0" -ex "thread apply all bt" --batch -p  <PID> | grep <LWPID> -A 10
Thread 49 (Thread 0x40781940 (LWP 19278)):
//                                ^-- light weight process id = 19278
#0  0x0000003bc8a0ab00 in pthread_cond_timedwait@@GLIBC_2.3.2 () from /lib64/libpthread.so.0
#1  0x00002ad0cd0370e6 in os::PlatformEvent::park () from /usr/java/jdk1.6.0_25/jre/lib/amd64/server/libjvm.so
#2  0x00002ad0cd008abb in Monitor::IWait () from /usr/java/jdk1.6.0_25/jre/lib/amd64/server/libjvm.so
#3  0x00002ad0cd00914e in Monitor::wait () from /usr/java/jdk1.6.0_25/jre/lib/amd64/server/libjvm.so
#4  0x00002ad0cd193df3 in VMThread::loop () from /usr/java/jdk1.6.0_25/jre/lib/amd64/server/libjvm.so
#5  0x00002ad0cd1939ee in VMThread::run () from /usr/java/jdk1.6.0_25/jre/lib/amd64/server/libjvm.so
#6  0x00002ad0cd03796f in java_start () from /usr/java/jdk1.6.0_25/jre/lib/amd64/server/libjvm.so
#7  0x0000003bc8a06367 in start_thread () from /lib64/libpthread.so.0
#8  0x0000003bc7ed30ad in clone () from /lib64/libc.so.6
```

## 五、应用占用内存过高，发生OOME异常 ##
遇到这种问题我们首先需要判断是哪个区内存OOME。

JVM内存主要分为：
  1. JVM管理的内存
    1. Heap区内存
    1. Perm区内存(JDK8开始将不再有Perm区)<br>主要是由Java类和字符串常量（或调用<code>String.intern()</code>）组成<br>
<ol><li>Direct Buffer内存<br>间接通过GC管理，即当持有对应Direct Buffer的Java对象在回收时才会对其回收<br>
</li></ol><ol><li>非JVM管理的内存<br>
<ol><li>Native内存<br>
<ol><li>线程创建占用的内存<br>
</li><li>JNI程序占用的内存</li></ol></li></ol></li></ol>

通过增加以下JVM参数，打开GC的相关详细信息到文件：<br>
<pre><code>-Xloggc:gc.log -XX:+PrintGCDetails -XX:+PrintGCTimeStamps<br>
</code></pre>

通过为JVM增加参数，可以在JVM发生OOM的时候将内部数据Dump到文件，输出同命令 <code>jmap</code> :<br>
<pre><code>-XX:+HeapDumpOnOutOfMemoryError<br>
</code></pre>

可以通过 <code>jstat -gcutil</code> 查找各个代的使用情况。<br>
<table><thead><th> <code>O + E + S0/1</code> </th><th> 表示Heap区 </th></thead><tbody>
<tr><td> P </td><td> 表示Perm区</td></tr></tbody></table>

<pre><code>  S0     S1     E      O      P     YGC     YGCT    FGC    FGCT     GCT   <br>
  0.00 100.00   6.47   0.21  81.13      1    0.015     0    0.000    0.015<br>
</code></pre>
<ul><li>Heap区OOME<br>
</li></ul><blockquote>Heap区对象创建过多，一般情况是程序的问题，也有可能是 <code>-Xmx</code> 大小设计过小。这种情况也可使用 <code>jtop</code> 查看，通过参数 <code>--sortmem</code> ，查找对的Java代码。还可以通过 <code>jmap -histo</code> 查找每个类的实例数据及占用的内存大小，也可通过 <code>histodiff</code> 查找该断时间内创建的对象情况。命令 <code>jmap -histo:live &lt;PID&gt;</code> 可以强制JVM做GC，通过多次这个命令可以看看内存中的对象是否会被回收。如果最终还是没有办法，可通过命令 <code>jmap -dump:format=b,file=&lt;file name&gt; &lt;PID&gt;</code> 将内存映象层出为文件，然后通过 <code>mat</code> 分析。<br>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/java/mat.png' />
</blockquote><ul><li>Perm区OOME<br>
</li></ul><blockquote>正常情况下Perm区的大小应该大于<code>&lt;size of lib&gt; * 2</code> ，如你的lib大小为<code>80M</code>，则你的Perm需要设置<code>&gt;160M</code>，生产环境应用一般设置<code>-XX:PermSize=256M</code>，当然具体需要视应用情况而定(需要分析应用占用Perm区的情况)。<br>
<ul><li>加载过多类，通过增加启动参数查找JVM加载了哪些类：<br>
<ul><li><code>-verbose:class</code> 相当于 <code>+TraceClassLoading,+TraceClassUnloading</code>
</li><li><code>+TraceClassLoading</code> 当类加载时打印日志<br>
</li></ul><blockquote>日志格式则是 <code>类名 + 加载的jar包名字</code> ：<br>
<pre><code>[Loaded sun.dc.pr.Rasterizer from /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/classes.jar]<br>
[Loaded sun.dc.pr.PathFiller from /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/classes.jar]<br>
[Loaded sun.dc.pr.PathDasher from /System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/classes.jar]<br>
</code></pre>
而对于下面这些类(from 为 <code>__JVM_DefineClass__</code>)则是没有具体的jar包，GeneratedMethodAccessor相关的类则是类反射调用次数超过15次自JVM自动增强产生的，日志如下：<br>
<pre><code>[Loaded sun.reflect.GeneratedMethodAccessor17 from __JVM_DefineClass__]<br>
[Loaded sun.reflect.GeneratedMethodAccessor18 from __JVM_DefineClass__]<br>
[Loaded sun.reflect.GeneratedMethodAccessor19 from __JVM_DefineClass__]<br>
</code></pre>
</blockquote><ul><li><code>+TraceClassUnloading</code> 当类卸载时打印日志<br>
</li></ul></li></ul><blockquote>也可通过<code>classdump</code> 或 <code>classlist</code> 列出加载的类的情况。<br>
</blockquote><ul><li>String.intern() 加载过多字符串<br>
<ul><li>通过 <code>permstat</code> 可以分析当前JVM中加载的String.intern()的情况<br>
</li><li>对于动态加载情况可以通过 btrace 跟踪加载情况<br>
</li></ul></li></ul></blockquote><ul><li>Native OOME<br>
<ul><li><a href='http://www-01.ibm.com/support/docview.wss?uid=swg21227106'>ZipOutputStream使用时未关闭会引起native内存无法正常释放</a>
</li><li><a href='Blog_FontCreateCauseOOME.md'>Font.createFont函数创建的Font对象会Soft引用Native内存</a>
</li></ul></li><li>Direct Buffer OOME<br>
</li></ul><blockquote>通过 <code>directbufferana</code> 查看当前JVM的 Direct Buffer 使用情况，如命令 <code>java -jar directbufferanaall.jar &lt;PID&gt;</code> 输出：<br>
<pre><code>NIO direct memory: (in bytes)<br>
  reserved size = 0.014094 MB (14779 bytes)<br>
  max size      = 123.937500 MB (129957888 bytes)<br>
  malloc'd size = 0.014001 MB (14681 bytes)<br>
</code></pre></blockquote>

<h2>进一步学习</h2>
<ul><li><a href='http://docs.oracle.com/javase/7/docs/webnotes/tsg/TSG-VM/html/toc.html'>http://docs.oracle.com/javase/7/docs/webnotes/tsg/TSG-VM/html/toc.html</a>
</li><li><a href='http://docs.oracle.com/javase/7/docs/webnotes/tsg/'>http://docs.oracle.com/javase/7/docs/webnotes/tsg/</a></li></ul>

<h3>参考资料</h3>
<code>[1].</code> <a href='Study_Java_HotSpot_OOME.md'>Study_Java_HotSpot_OOME</a><br>
<code>[2].</code> <a href='Study_Java_HotSpot_Arguments.md'>Study_Java_HotSpot_Arguments</a><br>
<code>[3].</code> <a href='http://java.decompiler.free.fr/'>http://java.decompiler.free.fr/</a><br>
<code>[4].</code> <a href='http://www.oracle.com/technetwork/java/javase/gc-tuning-6-140523.html'>http://www.oracle.com/technetwork/java/javase/gc-tuning-6-140523.html</a><br>
<code>[5].</code> <a href='http://www.oracle.com/technetwork/java/example-141412.html'>http://www.oracle.com/technetwork/java/example-141412.html</a><br>
<code>[6].</code> <a href='http://kenwublog.com/avoid-full-gc-in-hbase-using-arena-allocation'>http://kenwublog.com/avoid-full-gc-in-hbase-using-arena-allocation</a><br>
<code>[7].</code> <a href='http://blog.cloudera.com/blog/2011/02/avoiding-full-gcs-in-hbase-with-memstore-local-allocation-buffers-part-1/'>http://blog.cloudera.com/blog/2011/02/avoiding-full-gcs-in-hbase-with-memstore-local-allocation-buffers-part-1/</a><br>
<code>[8].</code> <a href='http://blog.cloudera.com/blog/2011/02/avoiding-full-gcs-in-hbase-with-memstore-local-allocation-buffers-part-2/'>http://blog.cloudera.com/blog/2011/02/avoiding-full-gcs-in-hbase-with-memstore-local-allocation-buffers-part-2/</a><br>
<code>[9].</code> <a href='http://blog.cloudera.com/blog/2011/03/avoiding-full-gcs-in-hbase-with-memstore-local-allocation-buffers-part-3/'>http://blog.cloudera.com/blog/2011/03/avoiding-full-gcs-in-hbase-with-memstore-local-allocation-buffers-part-3/</a><br>