## HotSpot中OutOfMemoryError解析 ##

在JVM中内存一共有3种：Heap（堆内存），Non-Heap（非堆内存） <sup>[3]</sup>和Native（本地内存）。 <sup>[1]</sup>

堆内存是运行时分配所有类实例和数组的一块内存区域。非堆内存包含方法区和JVM内部处理或优化所需的内存，存放有类结构（如运行时常量池、字段及方法结构，以及方法和构造函数代码）。本地内存是由操作系统管理的虚拟内存。当一个应用内存不足时就会抛出 `java.lang.OutOfMemoryError` 异常。 <sup>[1]</sup>

| **问题** | **表象** | **诊断工具** |
|:-----------|:-----------|:-----------------|
| 内存不足 | `OutOfMemoryError` | `Java Heap Analysis Tool(jhat)` <sup>[4]</sup><br><code>Eclipse Memory Analyzer(mat)</code> <sup>[5]</sup> <br>
<tr><td> 内存泄漏 </td><td> 使用内存增长，频繁GC </td><td> <code>Java Monitoring and Management Console(jconsole)</code> <sup>[6]</sup><br><code>JVM Statistical Monitoring Tool(jstat)</code> <sup>[7]</sup> </td></tr>
<tr><td> <code> </code> </td><td> 一个类有大量的实例 </td><td> <code>Memory Map(jmap) - "jmap -histo"</code> <sup>[8]</sup> </td></tr>
<tr><td> <code> </code> </td><td> 对象被误引用 </td><td> <code>jconsole</code> <sup>[6]</sup> 或 <code>jmap -dump + jhat</code> <sup>[8][4]</sup> </td></tr>
<tr><td> <code>Finalizers</code> </td><td> 对象等待结束 </td><td> <code>jconsole</code> <sup>[6]</sup> 或 <code>jmap -dump + jhat</code> <sup>[8][4]</sup> </td></tr></tbody></table>

当Java进程无法分配足够内存运行时将会抛出OutOfMemoryError：

#### 1. **`java.lang.OutOfMemoryError: Java heap space`** ####
> 堆内存溢出时，首先判断当前最大内存是多少（参数：`-Xmx` 或 `-XX:MaxHeapSize=`），可以通过命令 `jinfo -flag MaxHeapSize` <sup>[9]</sup>查看运行中的JVM的配置，如果该值已经较大则应通过 mat <sup>[5]</sup> 之类的工具查找问题，或 `jmap -histo` <sup>[8]</sup>查找哪个或哪些类占用了比较多的内存。参数`-verbose:gc(-XX:+PrintGC) -XX:+PrintGCDetails`可以打印GC相关的一些数据。如果问题比较难排查也可以通过参数`-XX:+HeapDumpOnOutOfMemoryError`在OOM之前Dump内存数据再进行分析。此问题也可以通过[histodiff](histodiff.md)打印多次内存histogram之前的差值，有助于查看哪些类过多被实例化，如果过多被实例化的类被定位到后可以通过btrace再跟踪。 <sup>[1][2]</sup><br>
<blockquote>下面代码可再现该异常： <sup>[2]</sup><br>
<pre><code>List&lt;String&gt; list = new ArrayList&lt;String&gt;();<br>
while(true) list.add(new String("Consume more memory!"));<br>
</code></pre></blockquote>

<h4>2. <b><code>java.lang.OutOfMemoryError: PermGen space</code></b></h4>
<blockquote><code>PermGen space</code>即永久代，是非堆内存的一个区域。主要存放的数据是类结构及调用了<code>intern()</code>的字符串。 <sup>[2]</sup><br>
<pre><code>List&lt;Class&lt;?&gt;&gt; classes = new ArrayList&lt;Class&lt;?&gt;&gt;();<br>
while(true){<br>
    MyClassLoader cl = new MyClassLoader();<br>
    try{<br>
        classes.add(cl.loadClass("Dummy"));<br>
    }catch (ClassNotFoundException e) {<br>
        e.printStackTrace();<br>
    }<br>
}<br>
</code></pre>
类加载的日志可以通过btrace跟踪类的加载情况：<br>
<pre><code>import com.sun.btrace.annotations.*;<br>
import static com.sun.btrace.BTraceUtils.*;<br>
<br>
@BTrace<br>
public class ClassLoaderDefine {<br>
<br>
    @SuppressWarnings("rawtypes")<br>
    @OnMethod(clazz = "+java.lang.ClassLoader", method = "defineClass", location = @Location(Kind.RETURN))<br>
    public static void onClassLoaderDefine(@Return Class cl) {<br>
        println("=== java.lang.ClassLoader#defineClass ===");<br>
        println(Strings.strcat("Loaded class: ", Reflective.name(cl)));<br>
        jstack(10);<br>
    }<br>
}<br>
</code></pre>
除了btrace也可以打开日志加载的参数来查看加载了哪些类，可以把参数<code>-XX:+TraceClassLoading</code>打开，或使用参数<code>-verbose:class</code>（<code>-XX:+TraceClassLoading, -XX:+TraceClassUnloading</code>），在日志输出中即可看到哪些类被加载到Java虚拟机中。该参数也可以通过<a href='jflag.md'>jflag</a>的命令<code>java -jar jflagall.jar -flag +ClassVerbose</code>动态打开<code>-verbose:class</code>。</blockquote>

<blockquote>下面是一个使用了<code>String.intern()</code>的例子： <sup>[2]</sup><br>
<pre><code>List&lt;String&gt; list = new ArrayList&lt;String&gt;();<br>
int i=0;<br>
while(true) list.add(("Consume more memory!"+(i++)).intern());<br>
</code></pre>
你可以通过以下btrace脚本查找该类调用：<br>
<pre><code>import com.sun.btrace.annotations.*;<br>
import static com.sun.btrace.BTraceUtils.*;<br>
<br>
@BTrace<br>
public class StringInternTrace {<br>
<br>
    @OnMethod(clazz = "/.*/", method = "/.*/",<br>
              location = @Location(value = Kind.CALL, clazz = "java.lang.String", method = "intern"))<br>
    public static void m(@ProbeClassName String pcm, @ProbeMethodName String probeMethod,<br>
                         @TargetInstance Object instance) {<br>
        println(strcat(pcm, strcat("#", probeMethod)));<br>
        println(strcat("&gt;&gt;&gt;&gt; ", str(instance)));<br>
    }<br>
}<br>
</code></pre></blockquote>

<h4>3. <b><code>java.lang.OutOfMemoryError: unable to create new native thread</code></b></h4>
<blockquote>在JVM中每启动一个线程都会分配一块本地内存，用于存放线程的调用栈，该空间仅在线程结束时释放。当没有足够本地内存创建线程时就会出现该错误。通过以下代码可以很容易再现该问题： <sup>[2]</sup><br>
<pre><code> while(true){<br>
    new Thread(new Runnable(){<br>
        public void run() {<br>
            try {<br>
                Thread.sleep(60*60*1000);<br>
            } catch(InterruptedException e) { }        <br>
        }    <br>
    }).start();<br>
}<br>
</code></pre></blockquote>

<h4>4. <b><code>java.lang.OutOfMemoryError: Direct buffer memory</code></b></h4>
<blockquote>即从Direct Memory分配内存失败，Direct Buffer对象不是分配在堆上，是在Direct Memory分配，且不被GC直接管理的空间（但Direct Buffer的Java对象是归GC管理的，只要GC回收了它的Java对象，操作系统才会释放Direct Buffer所申请的空间）。通过<code>-XX:MaxDirectMemorySize=</code>可以设置Direct内存的大小。 <sup>[2][10]</sup><br>
<pre><code>List&lt;ByteBuffer&gt; list = new ArrayList&lt;ByteBuffer&gt;();<br>
while(true) list.add(ByteBuffer.allocateDirect(10000000)); <br>
</code></pre></blockquote>

<h4>5. <b><code>java.lang.OutOfMemoryError: GC overhead limit exceeded</code></b></h4>
<blockquote>JDK6新增错误类型。当GC为释放很小空间占用大量时间时抛出。一般是因为堆太小。导致异常的原因：没有足够的内存。可以通过参数<code>-XX:-UseGCOverheadLimit</code>关闭这个特性。 <sup>[13]</sup></blockquote>

<h4>6. <b><code>java.lang.OutOfMemoryError: Requested array size exceeds VM limit</code></b></h4>
<blockquote>详细信息表示应用申请的数组大小已经超过堆大小。如应用程序申请512M大小的数组，但堆大小只有256M，这里会抛出<code>OutOfMemoryError</code>，因为此时无法突破虚拟机限制分配新的数组。在大多少情况下是堆内存分配的过小，或是应用尝试分配一个超大的数组，如应用使用的算法计算了错误的大小。 <sup>[12]</sup></blockquote>

<h4>7. <b><code>java.lang.OutOfMemoryError: request &lt;size&gt; bytes for &lt;reason&gt;. Out of swap space?</code></b></h4>
<blockquote>本地内存分配失败。一个应用的Java Native Interface(JNI)代码、本地库及Java虚拟机都从本地堆分配内存分配空间。当从本地堆分配内存失败时抛出OutOfMemoryError异常。例如：当物理内存及交换分区都用完后，再次尝试从本地分配内存时也会抛出OufOfMemoryError异常。 <sup>[12]</sup></blockquote>

<h4>8. <b><code>java.lang.OutOfMemoryError: &lt;reason&gt; &lt;stack trace&gt; (Native method)</code></b></h4>
<blockquote>如果异常的详细信息是 <code>&lt;reason&gt; &lt;stack trace&gt; (Native method)</code> 且一个线程堆栈被打印，同时最顶端的桢是本地方法，该异常表明本地方法遇到了一个内存分配问题。与前面一种异常相比，他们的差异是内存分配失败是JNI或本地方法发现或是Java虚拟机发现。 <sup>[12]</sup></blockquote>


<h3>参考资料</h3>
<code>[1].</code> <a href='http://java.sun.com/developer/technicalArticles/J2SE/monitoring/'>http://java.sun.com/developer/technicalArticles/J2SE/monitoring/</a><br>
<code>[2].</code> <a href='http://eyalsch.wordpress.com/2009/06/17/oome/'>http://eyalsch.wordpress.com/2009/06/17/oome/</a><br>
<code>[3].</code> <a href='http://docs.oracle.com/javase/6/docs/api/java/lang/management/MemoryType.html'>http://docs.oracle.com/javase/6/docs/api/java/lang/management/MemoryType.html</a><br>
<code>[4].</code> <a href='http://docs.oracle.com/javase/6/docs/technotes/tools/share/jhat.html'>http://docs.oracle.com/javase/6/docs/technotes/tools/share/jhat.html</a><br>
<code>[5].</code> <a href='http://www.eclipse.org/mat/'>http://www.eclipse.org/mat/</a><br>
<code>[6].</code> <a href='http://docs.oracle.com/javase/6/docs/technotes/tools/share/jconsole.html'>http://docs.oracle.com/javase/6/docs/technotes/tools/share/jconsole.html</a><br>
<code>[7].</code> <a href='http://docs.oracle.com/javase/6/docs/technotes/tools/share/jstat.html'>http://docs.oracle.com/javase/6/docs/technotes/tools/share/jstat.html</a><br>
<code>[8].</code> <a href='http://docs.oracle.com/javase/6/docs/technotes/tools/share/jmap.html'>http://docs.oracle.com/javase/6/docs/technotes/tools/share/jmap.html</a><br>
<code>[9].</code> <a href='http://docs.oracle.com/javase/6/docs/technotes/tools/share/jinfo.html'>http://docs.oracle.com/javase/6/docs/technotes/tools/share/jinfo.html</a><br>
<code>[10].</code> <a href='http://eyesmore.iteye.com/blog/1133335'>http://eyesmore.iteye.com/blog/1133335</a><br>
<code>[11].</code> <a href='http://www.oracle.com/technetwork/java/javase/index-137495.html'>http://www.oracle.com/technetwork/java/javase/index-137495.html</a><br>
<code>[12].</code> <a href='http://www.oracle.com/technetwork/java/javase/memleaks-137499.html'>http://www.oracle.com/technetwork/java/javase/memleaks-137499.html</a><br>
<code>[13].</code> <a href='http://blog.csdn.net/forandever/article/details/5717890'>http://blog.csdn.net/forandever/article/details/5717890</a><br>