## 属性定义 ##
属性名由以下字符组成：
```
abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_
```

## 运行模式 ##
| `-client`  | 即C1编译器，启动快，适合GUI程序 |
|:-----------|:---------------------------------------------|
| `-server`  | 即C2编译器，整体性能佳，适合服务器程序 |

```
$JAVA_HOME/jre/lib/amd64/jvm.cfg

Sample:
-server KNOWN
-client IGNORE
-hotspot ERROR
-classic WARN
-native ERROR
-green ERROR
```

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/client_server_plug.gif](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/client_server_plug.gif)

<sup>注</sup> C1和C2是两个不同的二进制代码系统。它们是一个系统下不同的编译(JIT)接口,C1系统优化的目的是最快的启动时间和小内存占用，C2系统优化目的是最佳总体运行性能。一般C1系统更适合交互的应用程序，如GUI程序。它们的不同步包括但不仅限于：编译策略，默认堆大小，内联策略。 <sup>[14]</sup>

## 编译模式 ##
| `-Xint` | 解释模式，仅解释运行 |
|:--------|:-------------------------------|
| `-Xcomp` | 编译模式，先编译再运行 |
| `-Xmixed` | 混合模式，也是默认模式 |

## Assertion Option <sup>[34]</sup> ##
| `-da, -disableassertions` | 禁用断言功能，不带参数时， -disableassertions 或 -da 开关选项禁用断言功能。 如果参数以"..."结尾，则在指定的包和子包中禁用断言功能。如果参数就是"..."，则在当前工作目录未命名的包中禁用断言功能。如果参数不是以"..."结尾，则在指定的类中禁用断言功能，Sample:<br>java -ea:a.b… -da:a.b.c <br>
<tr><td> <code>-ea, -enableassertions</code> </td><td> 启用断言 </td></tr>
<tr><td> <code>-dsa, -disablesystemassertions</code> </td><td> 在所有系统类中禁用断言功能 </td></tr>
<tr><td> <code>-esa, -enablesystemassertions</code> </td><td> 在所有系统类中启用断言功能 </td></tr></tbody></table>

<h2>标准参数？ ##
| **参数** | **具体参数** | **说明** |
|:-----------|:-----------------|:-----------|
| `-verbose:[class/gc/jni]` | `-verbose:class` | `-XX:+TraceClassLoading,-XX:+TraceClassUnloading` |
| `-` | `-verbose:gc` | `-XX:+PrintGC` |
| `-` | `-verbose:jni` | `-XX:+PrintJNIResolving` |
| `-Xbootclasspath[/a/p]:` | `-Xbootclasspath:` | 指定系统基本的需要的类库（包括rt.jar等），指定以分隔符分隔<sup>注1</sup>的文件夹，Jar包或Zip包 <sup>[8][9]</sup> |
| `-` | `-Xbootclasspath/a:` | 在`bootclasspath`**后**追加 |
| `-` | `-Xbootclasspath/p:` | 在`bootclasspath`**前**追加 |
| `-Xrun` | `-` |  |
| `-agentlib:` | `-` |  |
| `-agentpath:` | `-` |  |
| `-javaagent:` | `-` |  |
| `-Xnoclassgc` | `-` | `-XX:-ClassUnloading` |
| `-Xincgc` | `-` | `-XX:+UseConcMarkSweepGC,-XX:+CMSIncrementalMode` |
| `-Xnoincgc` | `-` | `-XX:+UseConcMarkSweepGC,-XX:+CMSIncrementalMode` |
| `-Xconcgc` | `-` | `-XX:+UseConcMarkSweepGC` |
| `-Xnoconcgc` | `-` | `-XX:+UseConcMarkSweepGC` |
| `-Xbatch` | `-` | `-XX:+BackgroundCompilation` |
| `-Xmx` | `-` | `-XX:MaxHeapSize=` <sup>注2</sup> |
| `-Xms` | `-` | `-XX:InitialHeapSize=` |
| `-Xmn` | `-` | `-XX:MaxNewSize=,-XX:NewSize=` |
| `-Xmaxf` | `-` | `-XX:MaxHeapFreeRatio=` |
| `-Xminf` | `-` | `-XX:MinHeapFreeRatio=` |
| `-Xss` | `-` | `-XX:ThreadStackSize=` |
| `-Xoss` | `-` | _该参数被忽略，HotSpot不使用区分的Native和Java栈_ |
| `-Xmaxjitcodesize` | `-` | `-XX:ReservedCodeCacheSize=` |
| `-green` | `-` | _HotSpot不支持Green线程，使用此参数无法启动JVM_ |
| `-native` | `-` | _该参数被忽略，HotSpot始终使用Native线程_ |
| `-Xsqnopause` | `-` | _该参数被忽略，注释为`EVM option`，但不知道是什么意思_ |
| `-Xrs` | `-` | `-XX:ReduceSignalUsage=` |
| `-Xusealtsigs` | `-` | _该参数被忽略，为向前兼容保留_ |
| `-Xoptimize` | `-` | _该参数被忽略，同`-Xsqnopause`_ |
| `-Xprof` | `-` |  |
| `-Xaprof` | `-` |  |
| `-Xconcurrentio` | `-` | `-XX:+UseLWPSynchronization,-XX:-BackgroundCompilation,`<br><code>-XX:DeferThrSuspendLoopCount=1,-XX:-UseTLAB,-XX:NewSizeThreadIncrease=16K</code> <br>
<tr><td> <code>-Xinternalversion</code> </td><td> <code>-</code> </td><td> 打印内部版本并退出 </td></tr>
<tr><td> <code>-Dcom.sun.management</code> </td><td> <code>-</code> </td><td> 设置<code>System.Property</code>参数的同时<code>-XX:+ManagementServer</code> </td></tr>
<tr><td> <code>-X[int/comp/mixed]</code> </td><td> <code>-Xint</code> </td><td>  </td></tr>
<tr><td> <code>-</code> </td><td> <code>-Xcomp</code> </td><td>  </td></tr>
<tr><td> <code>-</code> </td><td> <code>-Xmixed</code> </td><td>  </td></tr>
<tr><td> <code>-Xshare:[dump/on/auto/off]</code> </td><td> <code>-Xshare:dump</code> </td><td> <code>-XX:+DumpSharedSpaces</code> </td></tr>
<tr><td> <code>-</code> </td><td> <code>-Xshare:on</code> </td><td> <code>-XX:+UseSharedSpaces,-XX:+RequireSharedSpaces,-XX:+ForceSharedSpaces</code> </td></tr>
<tr><td> <code>-</code> </td><td> <code>-Xshare:auto</code> </td><td> <code>-XX:+UseSharedSpaces,-XX:-RequireSharedSpaces</code> </td></tr>
<tr><td> <code>-</code> </td><td> <code>-Xshare:off</code> </td><td> <code>-XX:-UseSharedSpaces,-XX:RequireSharedSpaces=</code> </td></tr>
<tr><td> <code>-Xverify:[all/remote/none]</code> </td><td> <code>-Xverify:all</code> </td><td> <code>-XX:+BytecodeVerificationLocal,-XX:+BytecodeVerificationRemote</code> </td></tr>
<tr><td> <code>-</code> </td><td> <code>-Xverify:remote</code> </td><td> <code>-XX:-BytecodeVerificationLocal,-XX:+BytecodeVerificationRemote</code> </td></tr>
<tr><td> <code>-</code> </td><td> <code>-Xverify:none</code> </td><td> <code>-XX:-BytecodeVerificationLocal,-XX:-BytecodeVerificationRemote</code> </td></tr>
<tr><td> <code>-Xdebug</code> </td><td> <code>-</code> </td><td>  </td></tr>
<tr><td> <code>-Xnoagent</code> </td><td> <code>-</code> </td><td> <i>该参数被忽略，为向前兼容保留</i> </td></tr>
<tr><td> <code>-Xboundthreads</code> </td><td> <code>-</code> </td><td> <code>-XX:+UseBoundThreads</code> </td></tr>
<tr><td> <code>-Xloggc:</code> </td><td> <code>-</code> </td><td> <code>-XX:+PrintGC,-XX:+PrintGCTimeStamps,-XX:+TraceClassUnloading</code><br>打印GC信息到指定文件 </td></tr>
<tr><td> <code>-Xcheck:jni</code> </td><td> <code>-</code> </td><td>  </td></tr></tbody></table>

<sup>注1.</sup> 分隔符在Linux/Unix操作系统和Winodws操作系统不一样，在Linux/Unix下为`":"`（冒号），在Windows下为`";"`（分号）<br>
<sup>注2.</sup> 当 <code>-Xmx != -Xms</code> 时，内部扩大或收缩需要通过一个Full GC来触发<br>

<h2>参数的类型</h2>
<pre><code> bool, intx, uintx, ccstr<br>
</code></pre>
在代码中其实还可以看到一种为 <code>double</code> 型，但使用并不广泛，而且在HotSpot中很多场景未判断该类型，使用时可能导致JVM异常退回，详见 <a href='http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7183920'>Bug ID 7183920</a>.<br>
<sup>注.</sup> <code>ccstr</code> 是 <code>const char*</code> 的别名<br>
<br>
<h2>参数运行时类型</h2>
<table><thead><th> <b>类型</b> </th><th> <b>说明</b> </th></thead><tbody>
<tr><td> <code>develop</code> </td><td> 开发参数，仅在开发版中可修改，在产品版则为常量，不可修改 </td></tr>
<tr><td> <code>develop_pd</code> </td><td> <b><i>应该是平台相关的开发属性，但未确认，求确认</i></b> </td></tr>
<tr><td> <code>product</code> </td><td> 产品参数，在各个编译版本中始终可修改 </td></tr>
<tr><td> <code>product_pd</code> </td><td> <b><i>应该是平台相关的产品属性，但未确认，求确认</i></b> </td></tr>
<tr><td> <code>lp64_product</code> </td><td> 64位产品参数，基本同产品参数，但仅在64位系统下可修改，在非64位系统下则为常量 </td></tr>
<tr><td> <code>notproduct</code> </td><td> 非产品参数，公在开发版中可修改，在产品版则未申明该参数 </td></tr>
<tr><td> <code>diagnostic</code> </td><td> 需要通过参数 <code>-XX:+UnlockDiagnosticVMOptions</code> 解锁<br>用于保障JVM质量及排查JVM Bug使用，但这些参数默认不可见，且不推荐使用，当需要使用时通过Unlock打开参数 </td></tr>
<tr><td> <code>experimental</code> </td><td> 需要通过参数 <code>-XX:+UnlockExperimentalVMOptions</code> 解锁<br>用于实验的参数，这些参数可能能够提升性能，但这些参数未经严格测试，这些参数也可以通过 <a href='https://code.google.com/p/hatter-source-code/wiki/jflag'>jflag</a> 打开 </td></tr>
<tr><td> <code>manageable</code> </td><td> 管理参数，为可运行时可修改的外部属性，参见 <b><code>[参数属性]</code></b> </td></tr>
<tr><td> <code>product_rw</code> </td><td> 产品运行时可写内部参数，参见 <b><code>[参数属性]</code></b> </td></tr></tbody></table>

<h2>参数属性</h2>
<table><thead><th> <code>external</code> </th><th> <code>manageable</code> 为属性为外部属性，其它属性都为内部属性 </th></thead><tbody>
<tr><td> <code>writeable</code> </td><td> <code>manageable</code> 及 <code>product_rw</code> 属性为运行时可写属性，即在JVM运行时动态可设置<br>对于<code>manageable</code>属性可以通过 <a href='http://docs.oracle.com/javase/6/docs/technotes/tools/share/jinfo.html'>jinfo -flag</a> 命令设置，对于 <code>product_rw</code> 属性可以通<code>HotSpotDiagnosticMXBean</code> JMX来设置，也可以通过<a href='https://code.google.com/p/hatter-source-code/wiki/jflag'>jflag</a>设置 </td></tr></tbody></table>

<h2>参数设置来源</h2>
<table><thead><th> <code>DEFAULT</code> </th><th> 系统默认值 </th></thead><tbody>
<tr><td> <code>COMMAND_LINE</code> </td><td> JVM创建时通过命令行指定，也称为<code>VM_CREATION</code> </td></tr>
<tr><td> <code>ENVIRON_VAR</code> </td><td> 根据系统环境指定，即在环境变量<code>_JAVA_OPTIONS</code>(Classic VM)或<code>JAVA_TOOL_OPTIONS</code>指定 <sup>[2]</sup> </td></tr>
<tr><td> <code>CONFIG_FILE</code> </td><td> 通过配置文件指定，通过参数<code>-XX:Flags=</code>指定，如果未指定则加载<code>.hotspotrc</code>配置文件 </td></tr>
<tr><td> <code>MANAGEMENT</code> </td><td> 通过<code>HotSpotDiagnosticMXBean</code>设置 </td></tr>
<tr><td> <code>ERGONOMIC</code> </td><td> 根据系统，或关联条件判断指定，如在未指定GC方式时JVM会根据<code>should_auto_select_low_pause_collector()</code>的结果，选择<code>UseConcMarkSweepGC</code>或<code>UseParallelGC</code> </td></tr>
<tr><td> <code>ATTACH_ON_DEMAND</code> </td><td> 通过<code>HotSpotVirtualMachine#setFlag(String name, String value)</code>设置 </td></tr>
<tr><td> <code>INTERNAL</code> </td><td> 通过<code>JVM_AccessVM{0}Flag</code>指定 (<code>{0}</code>取值为<code>Boolean</code>或<code>Int</code>)，但在源代码中未找到调用点，估计是给JNI或JVMTI使用 </td></tr></tbody></table>

<h2>参数详细说明</h2>
<table><thead><th> <b>参数名</b> </th><th> <b>参数类型</b> </th><th> <b>运行时类型</b> </th><th> <b>说明</b> </th></thead><tbody>
<tr><td> <code>UseCompressedOops</code> </td><td> <code>bool</code> </td><td> <code>lp64_product</code> </td><td> 在64位平台开启普通对象指针（oop<sup>注1</sup>）压缩，通常64位JVM消耗的内存会比32位的大1.5倍，这是因为对象指针在64位架构下，长度会翻倍（更宽的寻址）。对于那些将要从32位平台移植到64位的应用来说，平白无辜多了1/2的内存占用，这是开发者不愿意看到的。<br>启用CompressOops后，会压缩的对象：<br>1. 每个Class的属性指针（静态成员变量）<br>2. 每个对象的属性指针<br>3. 普通对象数组的每个元素指针<br>当然，压缩也不是万能的，针对一些特殊类型的指针，JVM是不会优化的。<br>比如指向PermGen的Class对象指针，本地变量，堆栈元素，入参，返回值，NULL指针不会被压缩。<br>3段式，根据<code>-Xmx/-XX:MaxHeapSize, -XX: HeapBaseMinAddress</code>以及进程环境自动选择：<br><code>// Narrow Oop encoding mode:</code><br><code>// 0 - UnscaledNarrowOop</code><br><code>//     - Use 32-bits oops without encoding when</code><br><code>//       HeapBaseMinAddress + heap_size &lt; 4Gb</code><br><code>// 1 - ZeroBasedNarrowOop</code><br><code>//     - Use zero based compressed oops with encoding when</code><br><code>//       HeapBaseMinAddress + heap_size &lt; 32Gb</code><br><code>// 2 - HeapBasedNarrowOop</code><br><code>//     - Use compressed oops with heap base + encoding.</code> <sup>[10][11][18][20]</sup> </td></tr>
<tr><td> <code>HeapBaseMinAddress</code> </td><td> <code>unitx</code> </td><td> <code>product_pd</code> </td><td> 在64位平台及使用了压缩指针时有效，参见 <code>UseCompressedOops</code> </td></tr>
<tr><td> <code>UseMembar</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> Without UseMembar option, in Linux, Hotspot uses memory serialize page instead of memory barrier instruction. Whenever a thread state transition happens, the thread writes to a memory address in memory serialize page with volatile pointer. When the VM thread needs to look at up-to-date state of all the threads, VM changes the protection bits for the memory serialize page to read only and then recovers it to read/write to serialize state changes.The use of <code>-XX:+UseMembar</code> causes the VM to revert back to true memory barrier instructions. <sup>[31]</sup> </td></tr>
<tr><td> <code>UnlockDiagnosticVMOptions</code> </td><td> <code>bool</code> </td><td> <code>diagnostic</code> </td><td> 解锁<code>diagnostic</code>类型的JVM参数，如<code>PrintInlining, PrintIntrinsics</code>等 </td></tr>
<tr><td> <code>UnlockExperimentalVMOptions</code> </td><td> <code>bool</code> </td><td> <code>experimental</code> </td><td> 解锁<code>experimental</code>类型的JVM参数，如<code>EnableMethodHandles, EnableInvokeDynamic</code>等 </td></tr>
<tr><td> <code>JavaMonitorsInStackTrace</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 在打印Java线程堆栈信息时同时打印锁信息，如 <code>- locked &lt;7f30011d8&gt; (a java.lang.ref.Reference$Lock)</code>，默认开启 </td></tr>
<tr><td> <code>UseLargePages</code> </td><td> <code>bool</code> </td><td> <code>product_pd</code> </td><td> 使用大页内存，该特性用于优化页表缓存<sup>注2</sup>，页表缓存在计算机中是稀少的资源，使用大页可以使用页表缓存可转译更大地址空间的物理内存。<br>不同的CPU支持不同的页大小：<br><code>i386: 4K and 4M (2M in PAE mode)</code><br><code>ia64: 4K, 8K, 64K, 256K, 1M, 4M, 16M, 256M</code><br><code>PPC64: 4K and 16M</code><br><code>POWER5+: 4K, 64K, 16MB, 16GB (!!)</code><br><code>UltraSparc III: 8K, 64K, 512K, 4M</code><br><code>UltraSparc T2: 8K, 64K, 4M, 256M</code><br>Linux在内核2.6版本以后支持大页，可以通过<code>cat /proc/meminfo | grep Huge</code>判断是否支持大页。<br>在某些应用系统中，开启使用大页有<code>25%~300%</code>不等的性能提升。 <sup>[21][22][23][24]</sup> </td></tr>
<tr><td> <code>LargePageSizeInBytes</code> </td><td> <code>uintx</code> </td><td> <code>product</code> </td><td> 设置大页的大小，当为0时由HotSpot自动选择大小 </td></tr>
<tr><td> <code>LargePageHeapSizeThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseLargePagesIndividualAllocation</code> </td><td> <code>bool</code> </td><td> <code>product_pd</code> </td><td> <i>仅在Windows下有效，具体作用还没看明白</i> </td></tr>
<tr><td> <code>UseNUMA</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 使用非均匀访存模型，非均匀访存模型（NUMA）的特点是：被共享的存储器物理上是分布式的，所有这些存储器的集合就是全局地址空间。所以处理器访问这些存储器的时间是不一样的，显然访问本地存储器的速度要比访问全局共享存储器或远程访问外地存储器要快些。另外，NUMA中存储器可能是分层的：本地存储器，群内共享存储器，全局共享存储器，在HotSpot@Linux中开启需要<br>1. 硬件是NUMA架构的；BIOS打开了NUMA选项<br>2. Xen等虚拟层的NUMA选项打开（如有）<br>3. 操作系统的NUMA选项打开<br>4. libnuma.so.1存在并且可加载<br>5. -XX:+UseParallelGC/-XX:+UseParallelOldGC <sup>[12][13]</sup> </td></tr>
<tr><td> <code>ForceNUMA</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 强制打开<code>UseNUMA</code> </td></tr>
<tr><td> <code>NUMAChunkResizeWeight</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NUMASpaceResizeRate</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseAdaptiveNUMAChunkSizing</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NUMAStats</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NUMAPageScanRate</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NeedsDeoptSuspend</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseSSE</code> </td><td> <code>intx</code> </td><td> <code>product</code> </td><td> <code>SSE</code>(<code>Streaming SIMD Extensions</code>)是英特尔在AMD的<code>3D Now!</code>发布一年之后，在其计算机芯片<code>Pentium III</code>中引入的指令集，是继MMX的扩充指令集。SSE 指令集提供了 70 条新指令。AMD后来在<code>Athlon XP</code>中加入了对这个新指令集的支持。<br>其后续版本有：<br><code>SSE2</code><br><code>SSE3</code><br><code>SSSE3</code><br><code>SSE4</code> 现在更新至<code>SSE4.2</code><br><code>SSE5</code><br><code>AVX</code><br><code>FMA</code> <sup>[25][26]</sup> </td></tr>
<tr><td> <code>UseSSE42Intrinsics</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 使用<code>SSE4.2</code>优化Java程序运行，打开此参数将对以下函数使用<code>SSE4.2</code>优化：<br><code>MacroAssembler::string_compare</code><br><code>MacroAssembler::char_arrays_equals</code><br><code>MacroAssembler::string_indexof</code> <sup>[27]</sup> </td></tr>
<tr><td> <code>ForceTimeHighResolution</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> <i>仅在Windows下有效，不清楚是干什么的</i> </td></tr>
<tr><td> <code>BackgroundCompilation</code> </td><td> <code>bool</code> </td><td> <code>product_pd</code> </td><td> 后台JIT编译优化启动 <sup>[32]</sup> </td></tr>
<tr><td> <code>PrintVMQWaitTime</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 打印等待执行的VMOperation的时间，当一个VMOperation有等待时间时，则打开这个VMOperation的名字和这它等待的时间(毫秒) </td></tr>
<tr><td> <code>MethodFlushing</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintJNIResolving</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseInlineCaches</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SystemMathNatives</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DeoptimizeRandom</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseCompilerSafepoints</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseSplitVerifier</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>FailOverToOldVerifier</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SafepointTimeout</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SuspendRetryCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SuspendRetryDelay</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AssertOnSuspendWaitFailure</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceSuspendWaitFailures</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxFDLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BytecodeVerificationRemote</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BytecodeVerificationLocal</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintOopAddress</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintGCApplicationConcurrentTime</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 打印每次垃圾回收前,程序未中断的执行时间 <sup>[33]</sup> </td></tr>
<tr><td> <code>PrintGCApplicationStoppedTime</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 打印垃圾回收期间程序暂停的时间 <sup>[33]</sup> </td></tr>
<tr><td> <code>ShowMessageBoxOnError</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseOSErrorReporting</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SuppressFatalErrorMessage</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>OnError</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>OnOutOfMemoryError</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>HeapDumpBeforeFullGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>HeapDumpAfterFullGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>HeapDumpOnOutOfMemoryError</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>HeapDumpPath</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseVectoredExceptions</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintCompilation</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 是否跟踪打印JIT编译信息 <sup>[35]</sup> </td></tr>
<tr><td> <code>AlwaysRestoreFPU</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>StackTraceInThrowable</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 当异常发生时收集异常堆栈 </td></tr>
<tr><td> <code>OmitStackTraceInFastThrow</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 在已经优化过(编译为本地代码)的代码中对经常抛出的异常不收集堆栈 </td></tr>
<tr><td> <code>ProfilerPrintByteCodeStatistics</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ProfilerRecordPC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ProfileVM</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ProfileIntervals</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintWarnings</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>RegisterFinalizersAtInit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ClassUnloading</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AlwaysLockClassLoader</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AllowParallelDefineClass</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MustCallLoadClassInternal</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DontYieldALot</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseBoundThreads</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 绑定用户级线程（Solaris only），这个选项强制所有的Java线程在创建时都作为操作系统绑定的线程 <sup>[35]</sup> </td></tr>
<tr><td> <code>UseLWPSynchronization</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SyncKnobs</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>EmitSync</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AlwaysInflate</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MonitorBound</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MonitorInUseLists</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Atomics</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>FenceInstruction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SyncFlags</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SyncVerbose</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ClearFPUAtPark</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>hashCode</code> </td><td> <code>intx</code> </td><td> <code>product</code> </td><td> 计算HashCode（即Object#hashCode，System#identityHashCode）的模式选择，参见：<a href='http://hg.openjdk.java.net/jdk6/jdk6/hotspot/file/tip/src/share/vm/runtime/synchronizer.cpp'>synchronizer.cpp</a>#get_next_hash </td></tr>
<tr><td> <code>WorkAroundNPTLTimedWaitHang</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>FilterSpuriousWakeups</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NativeMonitorTimeout</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NativeMonitorFlags</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NativeMonitorSpinLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AdjustConcurrency</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ReduceSignalUsage</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AllowUserSignalHandlers</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 允许用户在应用层设置信号处理回调函数 <sup>[35]</sup> </td></tr>
<tr><td> <code>UseSignalChaining</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseAltSigs</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseSpinning</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 启用多线程自旋锁优化。详见 <b><a href='Study_Java_HotSpot_Glossay.md'>HotSpot词汇表-自适应自旋</a></b> <sup>[19]</sup> </td></tr>
<tr><td> <code>PreSpinYield</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PostSpinYield</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AllowJNIEnvProxy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>JNIDetachReleasesMonitors</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>RestoreMXCSROnJNICalls</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CheckJNICalls</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseFastJNIAccessors</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>EagerXrunInit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PreserveAllAnnotations</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>LazyBootClassLoader</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseNewLongLShift</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseStoreImmI16</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseAddressNop</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseXmmLoadAndClearUpper</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseXmmRegToRegMoveAll</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseXmmI2D</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseXmmI2F</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseXMMForArrayCopy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseUnalignedLoadStores</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>FieldsAllocationStyle</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CompactFields</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseBiasedLocking</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BiasedLockingStartupDelay</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BiasedLockingBulkRebiasThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BiasedLockingBulkRevokeThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BiasedLockingDecayTime</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceJVMTI</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>StressLdcRewrite</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceRedefineClasses</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>VerifyMergedCPBytecodes</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>HPILibPath</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceClassResolution</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceBiasedLocking</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceMonitorInflation</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Use486InstrsOnly</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseSerialGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseG1GC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseParallelGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseParallelOldGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseParallelOldGCCompacting</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseParallelDensePrefixUpdate</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>HeapMaximumCompactionInterval</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>HeapFirstMaximumCompactionCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseMaximumCompactionOnSystemGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParallelOldDeadWoodLimiterMean</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParallelOldDeadWoodLimiterStdDev</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseParallelOldGCDensePrefix</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParallelGCThreads</code> </td><td> <code>uintx</code> </td><td> <code>product</code> </td><td> 并行收集器的线程数，此值最好配置与处理器数目相等 同样适用于CMS <sup>[33]</sup> </td></tr>
<tr><td> <code>ConcGCThreads</code> </td><td> <code>unitx</code> </td><td> <code>product</code> </td><td> CMS收集器的线程数 </td></tr>
<tr><td> <code>YoungPLABSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>OldPLABSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>GCTaskTimeStampEntries</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AlwaysTenure</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 是否使用Surivor空间，当为真时Surivor空间不存放任何对象 </td></tr>
<tr><td> <code>NeverTenure</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ScavengeBeforeFullGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseConcMarkSweepGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ExplicitGCInvokesConcurrent</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ExplicitGCInvokesConcurrentAndUnloadsClasses</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>GCLockerInvokesConcurrent</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>GCLockerEdenExpansionPercent</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseCMSBestFit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseCMSCollectionPassing</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseParNewGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParallelGCVerbose</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParallelGCBufferWastePct</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParallelGCRetainPLAB</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TargetPLABWastePct</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PLABWeight</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ResizePLAB</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintPLAB</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParGCArrayScanChunk</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParGCUseLocalOverflow</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParGCTrimOverflow</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParGCDesiredObjsFromOverflowList</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSParPromoteBlocksToClaim</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>OldPLABWeight</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ResizeOldPLAB</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintOldPLAB</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSOldPLABMin</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSOldPLABMax</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSOldPLABNumRefills</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSOldPLABResizeQuicker</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSOldPLABToleranceFactor</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSOldPLABReactivityFactor</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSOldPLABReactivityCeiling</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AlwaysPreTouch</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSUseOldDefaults</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSYoungGenPerWorker</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>GCOverheadReporting</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>GCOverheadReportingPeriodMS</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSIncrementalMode</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSIncrementalDutyCycle</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSIncrementalPacing</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSIncrementalDutyCycleMin</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSIncrementalSafetyFactor</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSIncrementalOffset</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSExpAvgFactor</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMS_FLSWeight</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMS_FLSPadding</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>FLSCoalescePolicy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>FLSAlwaysCoalesceLarge</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSExtrapolateSweep</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMS_SweepWeight</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMS_SweepPadding</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMS_SweepTimerThresholdMillis</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSClassUnloadingEnabled</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSClassUnloadingMaxInterval</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSCompactWhenClearAllSoftRefs</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseCMSCompactAtFullCollection</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSFullGCsBeforeCompaction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSIndexedFreeListReplenish</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSReplenishIntermediate</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSSplitIndexedFreeListBlocks</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSLoopWarn</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MarkStackSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MarkStackSizeMax</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSMaxAbortablePrecleanLoops</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSMaxAbortablePrecleanTime</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSAbortablePrecleanMinWorkPerIteration</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSAbortablePrecleanWaitMillis</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSRescanMultiple</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSConcMarkMultiple</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSRevisitStackSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSAbortSemantics</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSParallelRemarkEnabled</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSParallelSurvivorRemarkEnabled</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPLABRecordAlways</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSConcurrentMTEnabled</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPermGenPrecleaningEnabled</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPrecleaningEnabled</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPrecleanIter</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPrecleanNumerator</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPrecleanDenominator</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPrecleanRefLists1</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPrecleanRefLists2</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPrecleanSurvivors1</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPrecleanSurvivors2</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPrecleanThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSCleanOnEnter</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSRemarkVerifyVariant</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSScheduleRemarkEdenSizeThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSScheduleRemarkEdenPenetration</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSScheduleRemarkSamplingRatio</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSSamplingGrain</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSScavengeBeforeRemark</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSWorkQueueDrainThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSWaitDuration</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSYield</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSBitMapYieldQuantum</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSDumpAtPromotionFailure</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPrintChunksInDump</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSPrintObjectsInDump</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BlockOffsetArrayUseUnallocatedBlock</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>RefDiscoveryPolicy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParallelRefProcEnabled</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ParallelRefProcBalancingEnabled</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSTriggerRatio</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSTriggerPermRatio</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSBootstrapOccupancy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSInitiatingOccupancyFraction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>InitiatingHeapOccupancyPercent</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSInitiatingPermOccupancyFraction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseCMSInitiatingOccupancyOnly</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSIsTooFullPercentage</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintPromotionFailure</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PreserveMarkStackSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseTLAB</code> </td><td> <code>bool</code> </td><td> <code>product_pd</code> </td><td> <code>Thread-Local Allocation Buffers(TLAB)</code>线程本地分配缓存，是一种在多线程程序中的技术，每个线程拥有一小块Eden中的内存区域，在内存分配时避免锁，加速内存分配速度。 <sup>[28][29]</sup> </td></tr>
<tr><td> <code>ResizeTLAB</code> </td><td> <code>bool</code> </td><td> <code>product_pd</code> </td><td> 为线程动态改变TLAB的大小 <sup>[29]</sup> </td></tr>
<tr><td> <code>ZeroTLAB</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>FastTLABRefill</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintTLAB</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 打印和TLAB各种相关的信息到控制台，通过参数<code>-XX:+PrintTLAB -XX:+Verbose</code>可打印每个线程动态改变TLAB的大小，大小变化都在Scavenge GC<sup>注3</sup>时 <sup>[29]</sup> </td></tr>
<tr><td> <code>TLABStats</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> <i>记录TLAB相关信息</i> </td></tr>
<tr><td> <code>PrintRevisitStats</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NeverActAsServerClassMachine</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AlwaysActAsServerClassMachine</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxRAM</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ErgoHeapSizeLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxRAMFraction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DefaultMaxRAMFraction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MinRAMFraction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>InitialRAMFraction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseAutoGCSelectPolicy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AutoGCSelectPauseMillis</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseAdaptiveSizePolicy</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> -XX:+UseAdaptiveSizePolicy 建议使用-XX:-UseAdaptiveSizePolicy关掉，为什么当你的参数设置了NewRatio、Survivor、MaxTenuringThreshold这几个参数如果在启动了动态更新情况下，是无效的，当然如果你设置-Xmn是有效的，但是如果设置的比例的话，初始化可能会按照你的参数去运行，不过运行过程中会通过一定的算法动态修改，监控中你可能会发现这些参数会发生改变，甚至于S0和S1的大小不一样，如果启动了这个参数，又想要跟踪变化，那么就使用参数：-XX:+PrintAdaptiveSizePolicy <sup>[32]</sup> </td></tr>
<tr><td> <code>UsePSAdaptiveSurvivorSizePolicy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseAdaptiveGenerationSizePolicyAtMinorCollection</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseAdaptiveGenerationSizePolicyAtMajorCollection</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseAdaptiveSizePolicyWithSystemGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseAdaptiveGCBoundary</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AdaptiveSizeThroughPutPolicy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AdaptiveSizePausePolicy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AdaptiveSizePolicyInitializingSteps</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AdaptiveSizePolicyOutputInterval</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseAdaptiveSizePolicyFootprintGoal</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AdaptiveSizePolicyWeight</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AdaptiveTimeWeight</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PausePadding</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PromotedPadding</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SurvivorPadding</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AdaptivePermSizeWeight</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PermGenPadding</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ThresholdTolerance</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AdaptiveSizePolicyCollectionCostMargin</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>YoungGenerationSizeIncrement</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>YoungGenerationSizeSupplement</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>YoungGenerationSizeSupplementDecay</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TenuredGenerationSizeIncrement</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TenuredGenerationSizeSupplement</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TenuredGenerationSizeSupplementDecay</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxGCPauseMillis</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>GCPauseIntervalMillis</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxGCMinorPauseMillis</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>GCTimeRatio</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AdaptiveSizeDecrementScaleFactor</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseAdaptiveSizeDecayMajorGCCost</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AdaptiveSizeMajorGCDecayTimeScale</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MinSurvivorRatio</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>InitialSurvivorRatio</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BaseFootPrintEstimate</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseGCOverheadLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>GCTimeLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>GCHeapFreeLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintAdaptiveSizePolicy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrefetchCopyIntervalInBytes</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrefetchScanIntervalInBytes</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrefetchFieldsAhead</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DisableExplicitGC</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 禁止在运行期显式地调用<code>System.gc()</code>。<br>开启该选项后，GC的触发时机将由Garbage Collector全权掌控。<br>注意：你熟悉的代码里没调用System.gc()，不代表你依赖的框架工具没在使用。<br>例如RMI就在多数用户毫不知情的情况下，显示地调用GC来防止自身OOM。<sup>[19]</sup> </td></tr>
<tr><td> <code>PreZeroEden</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CollectGen0First</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> FullGC时是否先YGC <sup>[33]</sup> </td></tr>
<tr><td> <code>BindGCTaskThreadsToCPUs</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseGCTaskAffinity</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ProcessDistributionStride</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSCoordinatorYieldSleepCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CMSYieldSleepCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintGCDetails</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintGCDateStamps</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintGCTimeStamps</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintGCTaskTimeStamps</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintReferenceGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceClassLoading</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceClassLoadingPreorder</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceClassUnloading</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceLoaderConstraints</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceGen0Time</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceGen1Time</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintTenuringDistribution</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 打印对象晋升相关的信息 </td></tr>
<tr><td> <code>PrintHeapAtGC</code> </td><td> <code>bool</code> </td><td> <code>product_rw</code> </td><td> 在GC前后打印Heap分带信息 </td></tr>
<tr><td> <code>PrintHeapAtGCExtended</code> </td><td> <code>bool</code> </td><td> <code>product_rw</code> </td><td> 当在G1回收算法GC时打印扩展信息 </td></tr>
<tr><td> <code>PrintHeapAtSIGBREAK</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintClassHistogramBeforeFullGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintClassHistogramAfterFullGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintClassHistogram</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceParallelOldGCTasks</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintParallelOldGCPhaseTimes</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintJNIGCStalls</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CITime</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CICompilerCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CompilationPolicyChoice</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintSafepointStatistics</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintSafepointStatisticsCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintSafepointStatisticsTimeout</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceSafepointCleanupTime</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Inline</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ClipInlining</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseTypeProfile</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TypeProfileMajorReceiverPercent</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseCountLeadingZerosInstruction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UsePopCountInstruction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintVMOptions</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 打印命令行设置的参数(未做转换) </td></tr>
<tr><td> <code>IgnoreUnrecognizedVMOptions</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 忽略JVM不认识的参数设置 </td></tr>
<tr><td> <code>PrintCommandLineFlags</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 打印启动参数中设置的JVM参数 </td></tr>
<tr><td> <code>PrintFlagsInitial</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 打印JVM参数初始值并退出 </td></tr>
<tr><td> <code>PrintFlagsFinal</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 打印最终的JVM参数 </td></tr>
<tr><td> <code>ErrorFile</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DisplayVMOutputToStderr</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DisplayVMOutputToStdout</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseHeavyMonitors</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>RangeCheckElimination</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SplitIfBlocks</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AggressiveOpts</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 加快编译 </td></tr>
<tr><td> <code>UseStringCache</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseCompressedStrings</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SpecialStringCompress</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SpecialStringInflate</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SpecialStringCompareToCC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SpecialStringIndexOfCC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SpecialStringEqualsCC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>RewriteBytecodes</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>RewriteFrequentPairs</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseInterpreter</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseNiagaraInstrs</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseLoopCounter</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseFastEmptyMethods</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseFastAccessorMethods</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 原始类型的快速优化 </td></tr>
<tr><td> <code>UseOnStackReplacement</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PreferInterpreterNativeStubs</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ProfileInterpreter</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ProfileMaturityPercentage</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseCompiler</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseCounterDecay</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AlwaysCompileLoopMethods</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DontCompileHugeMethods</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 不编译大方法（即字节数大于<code>HugeMethodLimit</code>的方法，默认8000，<code>develop</code>类型参数） </td></tr>
<tr><td> <code>EstimateArgEscape</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BCEATraceLevel</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxBCEAEstimateLevel</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxBCEAEstimateSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AllocatePrefetchStyle</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AllocatePrefetchDistance</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AllocatePrefetchLines</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AllocatePrefetchStepSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AllocatePrefetchInstr</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ReadPrefetchInstr</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintJavaStackAtFatalState</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SelfDestructTimer</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxJavaStackTraceDepth</code> </td><td> <code>intx</code> </td><td> <code>product</code> </td><td> 异常抛出时打印栈的最大深度 </td></tr>
<tr><td> <code>SafepointTimeoutDelay</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NmethodSweepFraction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NmethodSweepCheckInterval</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxInlineLevel</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxRecursiveInlineLevel</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>InlineSmallCode</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxInlineSize</code> </td><td> <code>intx</code> </td><td> <code>product</code> </td><td> 限制动态编译的内联函数的虚拟机指令的最大数量 <sup>[35]</sup> </td></tr>
<tr><td> <code>FreqInlineSize</code> </td><td> <code>intx</code> </td><td> <code>product_pd</code> </td><td> 限制经常使用的动态编译的函数的虚拟机指令的最大数量 <sup>[35]</sup> </td></tr>
<tr><td> <code>MaxTrivialSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MinInliningThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ProfileIntervalsTicks</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>EventLogLength</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TypeProfileWidth</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerMethodRecompilationCutoff</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerBytecodeRecompilationCutoff</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerMethodTrapLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerBytecodeTrapLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AliasLevel</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ReadSpinIterations</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PreInflateSpin</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PreBlockSpin</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>InitialHeapSize</code> </td><td> <code>uintx</code> </td><td> <code>product</code> </td><td> 为Heap区域的初始值 <sup>[32]</sup> </td></tr>
<tr><td> <code>MaxHeapSize</code> </td><td> <code>uintx</code> </td><td> <code>product</code> </td><td> 为Heap区域的最大值 <sup>[32]</sup> </td></tr>
<tr><td> <code>OldSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NewSize</code> </td><td> <code>uintx</code> </td><td> <code>product</code> </td><td> 设置年轻代大小 </td></tr>
<tr><td> <code>MaxNewSize</code> </td><td> <code>uintx</code> </td><td> <code>product</code> </td><td> 年轻代最大值 </td></tr>
<tr><td> <code>PretenureSizeThreshold</code> </td><td> <code>uintx</code> </td><td> <code>product</code> </td><td> 对象超过多大是直接在旧生代分配,单位字节 新生代采用Parallel Scavenge GC时无效，另一种直接在旧生代分配的情况是大的数组对象,且数组中无外部引用对象 <sup>[33]</sup></td></tr>
<tr><td> <code>TLABSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MinTLABSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TLABAllocationWeight</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TLABWasteTargetPercent</code> </td><td> <code>uintx</code> </td><td> <code>product</code> </td><td> TLAB占eden区的百分比 </td></tr>
<tr><td> <code>TLABRefillWasteFraction</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TLABWasteIncrement</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SurvivorRatio</code> </td><td> <code>intx</code> </td><td> <code>product</code> </td><td> 该参数为Eden与两个求助空间之一的比例，注意Yong的大小等价于Eden + S0 + S1，S0和S1的大小是等价的，这个参数为Eden与其中一个S区域的大小比例，如参数为8，那么Eden就占用Yong的80%，而S0和S1分别占用10% <sup>[32]</sup> </td></tr>
<tr><td> <code>NewRatio</code> </td><td> <code>intx</code> </td><td> <code>product</code> </td><td> 为Old区域为Yong的多少倍，间接设置Yong的大小，1.6中如果使用此参数，则默认会在适当时候被动态调整，具体请看下面参数UseAdaptiveSizePolicy 的说明 <sup>[32]</sup></td></tr>
<tr><td> <code>NewSizeThreadIncrease</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PermSize</code> </td><td> <code>uintx</code> </td><td> <code>product_pd</code> </td><td> 设置持久代(perm gen)初始值 </td></tr>
<tr><td> <code>MaxPermSize</code> </td><td> <code>uintx</code> </td><td> <code>product_pd</code> </td><td> 设置持久代最大值 </td></tr>
<tr><td> <code>MinHeapFreeRatio</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxHeapFreeRatio</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SoftRefLRUPolicyMSPerMB</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MinHeapDeltaBytes</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MinPermHeapExpansion</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxPermHeapExpansion</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>QueuedAllocationWarningCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxTenuringThreshold</code> </td><td> <code>intx</code> </td><td> <code>product</code> </td><td> 在正常情况下，新申请的对象在Yong区域发生多少次GC后就会被移动到Old（非正常就是S0或S1放不下或者不太可能出现的Eden都放不下的对象），这个参数一般不会超过16（因为计数器从0开始计数，所以设置为15的时候相当于生命周期为16），要查看现在的这个值的具体情况，可以使用参数：-XX:+PrintTenuringDistribution <sup>[32]</sup> </td></tr>
<tr><td> <code>InitialTenuringThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TargetSurvivorRatio</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MarkSweepDeadRatio</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PermMarkSweepDeadRatio</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MarkSweepAlwaysCompactCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintCMSStatistics</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintCMSInitiationStatistics</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintFLSStatistics</code> </td><td> <code>intx</code> </td><td> <code>product</code> </td><td> 取值： <code>0, 1, 2</code> 用于打印CMS GC时的空闲空间列表 </td></tr>
<tr><td> <code>PrintFLSCensus</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DeferThrSuspendLoopCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DeferPollingPageLoopCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SafepointSpinBeforeYield</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PSChunkLargeArrays</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>GCDrainStackTargetSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>StackYellowPages</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>StackRedPages</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>StackShadowPages</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ThreadStackSize</code> </td><td> <code>intx</code> </td><td> <code>product_pd</code> </td><td> 线程的栈大小，如在OpenJDK中使用函数“JavaThread::set_stack_size_at_create”(Linux)来设置该参数 </td></tr>
<tr><td> <code>VMThreadStackSize</code> </td><td> <code>intx</code> </td><td> <code>product_pd</code> </td><td> VMThread, PGCThread, CGCTHhread, WatcherThread线程的栈大小 </td></tr>
<tr><td> <code>CompilerThreadStackSize</code> </td><td> <code>intx</code> </td><td> <code>product_pd</code> </td><td> 编译线程的栈大小，如果未设置则使用VMThreadStackSize的大小 </td></tr>
<tr><td> <code>ThreadSafetyMargin</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>OptoLoopAlignment</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>InitialCodeCacheSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ReservedCodeCacheSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CodeCacheMinimumFreeSpace</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CodeCacheExpansionSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseCodeCacheFlushing</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MinCodeCacheFlushingInterval</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CodeCacheFlushingMinimumFreeSpace</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CompileOnly</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CompileCommandFile</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CompileCommand</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CICompilerCountPerCPU</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseThreadPriorities</code> </td><td> <code>bool</code> </td><td> <code>product_pd</code> </td><td> 设置是否使用本地线程优先级 <sup>[35]</sup> </td></tr>
<tr><td> <code>ThreadPriorityPolicy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ThreadPriorityVerbose</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DefaultThreadPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CompilerThreadPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>VMThreadPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CompilerThreadHintNoPreempt</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>VMThreadHintNoPreempt</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>JavaPriority1_To_OSPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>JavaPriority2_To_OSPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>JavaPriority3_To_OSPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>JavaPriority4_To_OSPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>JavaPriority5_To_OSPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>JavaPriority6_To_OSPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>JavaPriority7_To_OSPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>JavaPriority8_To_OSPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>JavaPriority9_To_OSPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>JavaPriority10_To_OSPriority</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>StarvationMonitorInterval</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CompileThreshold</code> </td><td> <code>intx</code> </td><td> <code>product_pd</code> </td><td> 触发JIT的阈值，函数被解释执行的次数 </td></tr>
<tr><td> <code>BackEdgeThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier0InvokeNotifyFreqLog</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier2InvokeNotifyFreqLog</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier3InvokeNotifyFreqLog</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier0BackedgeNotifyFreqLog</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier2BackedgeNotifyFreqLog</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier3BackedgeNotifyFreqLog</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier2CompileThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier2BackEdgeThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier3InvocationThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier3MinInvocationThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier3CompileThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier3BackEdgeThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier4InvocationThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier4MinInvocationThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier4CompileThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier4BackEdgeThreshold</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier3DelayOn</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier3DelayOff</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier3LoadFeedback</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier4LoadFeedback</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TieredCompileTaskTimeout</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TieredStopAtLevel</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier0ProfilingStartPercentage</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TieredRateUpdateMinTime</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TieredRateUpdateMaxTime</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TieredCompilation</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintTieredEvents</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>StressTieredRuntime</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>OnStackReplacePercentage</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>InterpreterProfilePercentage</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DesiredMethodLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ReflectionWrapResolutionErrors</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxDirectMemorySize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UsePerfData</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerfDataSaveToFile</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerfDataSaveFile</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerfDataSamplingInterval</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerfDisableSharedMem</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerfDataMemorySize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerfMaxStringConstLength</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerfAllowAtExitRegistration</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PerfBypassFileSystemCheck</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UnguardOnExecutionViolation</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ManagementServer</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DisableAttachMechanism</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 禁止JVM被Attach </td></tr>
<tr><td> <code>StartAttachListener</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintConcurrentLocks</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseSharedSpaces</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>RequireSharedSpaces</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DumpSharedSpaces</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintSharedSpaces</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SharedDummyBlockSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SharedReadWriteSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SharedReadOnlySize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SharedMiscDataSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SharedMiscCodeSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AnonymousClasses</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ExtendedDTraceProbes</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DTraceMethodProbes</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DTraceAllocProbes</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DTraceMonitorProbes</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>RelaxAccessControlCheck</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>WaitForDebugger</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseFileLocking</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UncommitYoungGenOnGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UncommitOldGenOnGC</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceUncommitMemory</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UncommitUsesMadvise</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseVMInterruptibleIO</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1ConfidencePercent</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1MarkRegionStackSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1SATBBufferSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1SATBBufferEnqueueingThresholdPercent</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1UpdateBufferSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1ConcRefinementYellowZone</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1ConcRefinementRedZone</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1ConcRefinementGreenZone</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1ConcRefinementServiceIntervalMillis</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1ConcRefinementThresholdStep</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1RSetUpdatingPauseTimePercent</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1UseAdaptiveConcRefinement</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1RSetRegionEntries</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1RSetSparseRegionEntries</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1ReservePercent</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1HeapRegionSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1ConcRefinementThreads</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>G1RSetScanBlockSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CacheGlobally</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CacheCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ValueMapInitialSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ValueMapMaxLoopSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>LIRFillDelaySlots</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TimeLinearScan</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>CompilationRepeat</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>LIRSchedule</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>SafepointPollOffset</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>C1ProfileCalls</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>C1ProfileVirtualCalls</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>C1ProfileInlinedCalls</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>C1ProfileBranches</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>C1ProfileCheckcasts</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>C1OptimizeVirtualCallProfiling</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>C1UpdateMethodData</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>InteriorEntryAlignment</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxLoopPad</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NumberOfLoopInstrToAlign</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>LoopUnrollLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>LoopUnrollMin</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MultiArrayExpandLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseLoopPredicate</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>OptimizeFill</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TrackedInitializationLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ReduceFieldZeroing</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ReduceInitialCardMarks</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ReduceBulkZeroing</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseFPUForSpilling</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>OptoScheduling</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PartialPeelLoop</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PartialPeelNewPhiDelta</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PartialPeelAtUnsignedTests</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ReassociateInvariants</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>LoopUnswitching</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseSuperWord</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>TraceSuperWord</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>OptoBundling</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ConditionalMoveLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BranchOnRegister</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseRDPCForConstantTableBase</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseOldInlining</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseBimorphicInlining</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseOnlyInlinedBimorphic</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>InsertMemBarAfterArraycopy</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier1Inline</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier1MaxInlineSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier1FreqInlineSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>Tier1LoopOptsCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>LoopOptsCount</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxNodeLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>NodeLimitFudgeFactor</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseJumpTables</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseDivMod</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MinJumpTableSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxJumpTableSize</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxJumpTableSparseness</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>EliminateLocks</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>PrintPreciseBiasedLockingStatistics</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>EliminateAutoBox</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>AutoBoxCacheMax</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DoEscapeAnalysis</code> </td><td> <code>bool</code> </td><td> <code>product</code> </td><td> 逃逸分析，在C2中定义，在编程语言的编译优化原理中，分析指针动态范围的方法称之为逃逸分析。通俗一点讲，就是当一个对象的指针被多个方法或线程引用时，我们称这个指针发生了逃逸。而用来分析这种逃逸现象的方法，就称之为逃逸分析。 <sup>[15][16][17]</sup> </td></tr>
<tr><td> <code>EliminateAllocations</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>EliminateAllocationArraySizeLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>UseOptoBiasInlining</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>OptimizeStringConcat</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>ValueSearchLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>MaxLabelRootDepth</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>DominatorSearchLimit</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BlockLayoutByFrequency</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BlockLayoutMinDiamondPercentage</code> </td><td> `` </td><td> `` </td><td>  </td></tr>
<tr><td> <code>BlockLayoutRotateLoops</code> </td><td> `` </td><td> `` </td><td>  </td></tr></tbody></table>

<sup>注1.</sup> <code>oop</code> 即 <code>ordinary object pointer</code>，普通对象指针，在HotSpot中管理指向对象的指针 <sup>[11]</sup><br>
<sup>注2.</sup> 页表缓存（英文：Translation Lookaside Buffer，缩写为：TLB），也译为转址旁路缓存或转译后备缓冲区，为CPU的一种缓存，由内存管理单元用于改进虚拟地址到物理地址的转译速度。目前所有的桌面电脑和服务器（如X86）都使用TLB。TLB有固定数量的空间区域，用于存放将虚拟地址映射到物理地址的分页表项。 <sup>[23]</sup><br>
<sup>注3.</sup> GC有两种类型：Scavenge GC和Full GC。一般情况下，当新对象生成，并且在Eden申请空间失败时，就好触发Scavenge GC，堆Eden区域进行GC，清除非存活对象，并且把尚且存活的对象移动到Survivor区。然后整理Survivor的两个区。Full GC对整个堆进行整理，包括Young、Tenured和Perm。Full GC比Scavenge GC要慢，因此应该尽可能减少Full GC。有如下原因可能导致Full GC：Tenured被写满、Perm域被写满、System.gc()被显示调用（HotSpot的行为是FullGC）<sup>30</sup><br>

<h2>System.Properties参数</h2>
<table><thead><th> <b>参数Key</b> </th><th> <b>说明</b> </th></thead><tbody>
<tr><td> <code>java.vm.specification.version</code> </td><td> <code>1.0</code> </td></tr>
<tr><td> <code>java.vm.specification.name</code> </td><td> <code>Java Virtual Machine Specification</code> </td></tr>
<tr><td> <code>java.vm.specification.vendor</code> </td><td> <code>Sun Microsystems Inc.</code> </td></tr>
<tr><td> <code>java.vm.version</code> </td><td> <code>VM_Version::vm_release()</code> </td></tr>
<tr><td> <code>java.vm.name</code> </td><td> <code>VM_Version::vm_name()</code> </td></tr>
<tr><td> <code>java.vm.vendor</code> </td><td> <code>VM_Version::vm_vendor()</code> </td></tr>
<tr><td> <code>java.vm.info</code> </td><td> <code>VM_Version::vm_info_string()</code> </td></tr>
<tr><td> <code>java.ext.dirs</code> </td><td> 可选包扩展机制，可通过<code>jvm_args</code>参数设置，可指定一个或多个文件夹，默认设置则为标准可选包安装目录 <sup>[5][6]</sup> </td></tr>
<tr><td> <code>java.endorsed.dirs</code> </td><td> 包升级替换机制，可通过<code>jvm_args</code>参数设置，可指定一个或多个文件夹，默认设置为<code>&lt;java-home&gt;/lib/endorsed</code> <sup>[5][7]</sup> </td></tr>
<tr><td> <code>sun.boot.library.path</code> </td><td>  </td></tr>
<tr><td> <code>java.library.path</code> </td><td>  </td></tr>
<tr><td> <code>java.home</code> </td><td>  </td></tr>
<tr><td> <code>sun.boot.class.path</code> </td><td>  </td></tr>
<tr><td> <code>java.class.path</code> </td><td> <b>不可通过<code>jvm_args</code>设置</b> </td></tr></tbody></table>

<table><thead><th> <b>参数Key</b> </th><th> <b>说明</b> </th></thead><tbody>
<tr><td> <code>java.compiler</code> </td><td>  </td></tr>
<tr><td> <code>sun.java.command</code> </td><td> <b>不可通过<code>jvm_args</code>设置</b> </td></tr>
<tr><td> <code>sun.java.launcher</code> </td><td> <b>不可通过<code>jvm_args</code>设置</b> </td></tr>
<tr><td> <code>sun.java.launcher.pid</code> </td><td>  </td></tr>
<tr><td> <code>java.vendor.url.bug</code> </td><td>  </td></tr>
<tr><td> <code>sun.boot.library.path</code> </td><td>  </td></tr></tbody></table>

<h2>废弃的参数</h2>
<table><thead><th> <b>参数</b> </th><th> <b>开始弃用版本</b> </th><th> <b>禁止使用版本</b> </th><th> <b>说明</b> </th></thead><tbody>
<tr><td> <code>UseTrainGC</code> </td><td> 1.5 </td><td> 1.7 </td><td> incremental (有时也称为train) low pause collector：如果要使用它，则可通过命令行-XX:+UseTrainGC来激活。这个collector从J2SE Platform 1.4.2版本之后就不再改变了，同时也停止了开发活动。 <sup>[3][4]</sup> </td></tr>
<tr><td> <code>UseSpecialLargeObjectHandling</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>UseOversizedCarHandling</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>TraceCarAllocation</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>PrintTrainGCProcessingStats</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>LogOfCarSpaceSize</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>OversizedCarThreshold</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>MinTickInterval</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>DefaultTickInterval</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>MaxTickInterval</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>DelayTickAdjustment</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>ProcessingToTenuringRatio</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>MinTrainLength</code> </td><td> 1.5 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>AppendRatio</code> </td><td> 1.6u10 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>DefaultMaxRAM</code> </td><td> 1.6u18 </td><td> 1.7 </td><td>  </td></tr>
<tr><td> <code>DefaultInitialRAMFraction</code> </td><td> 1.6u18 </td><td> 1.7 </td><td>  </td></tr></tbody></table>

<a href='Hidden comment:  不要在参考资料中插入或删除引用 '></a><br>
<h3>参考资料</h3>
<code>[1].</code> openjdk-6-src-b20-21_jun_2010.tar.gz<br>
<code>[2].</code> <a href='http://docs.oracle.com/javase/1.5.0/docs/guide/jvmti/jvmti.html#tooloptions'>http://docs.oracle.com/javase/1.5.0/docs/guide/jvmti/jvmti.html#tooloptions</a><br>
<code>[3].</code> <a href='http://www.cnblogs.com/z-zw/archive/2010/09/30/1839394.html'>http://www.cnblogs.com/z-zw/archive/2010/09/30/1839394.html</a><br>
<code>[4].</code> <a href='http://www.oracle.com/technetwork/java/gc1-4-2-135950.html'>http://www.oracle.com/technetwork/java/gc1-4-2-135950.html</a><br>
<code>[5].</code> <a href='http://blog.csdn.net/flyingstarwb/article/details/7018085'>http://blog.csdn.net/flyingstarwb/article/details/7018085</a><br>
<code>[6].</code> <a href='http://docs.oracle.com/javase/1.4.2/docs/guide/extensions/spec.html'>http://docs.oracle.com/javase/1.4.2/docs/guide/extensions/spec.html</a><br>
<code>[7].</code> <a href='http://docs.oracle.com/javase/6/docs/technotes/guides/standards/index.html'>http://docs.oracle.com/javase/6/docs/technotes/guides/standards/index.html</a><br>
<code>[8].</code> <a href='http://docs.oracle.com/javase/6/docs/technotes/tools/solaris/java.html'>http://docs.oracle.com/javase/6/docs/technotes/tools/solaris/java.html</a><br>
<code>[9].</code> <a href='http://docs.oracle.com/javase/6/docs/technotes/tools/findingclasses.html'>http://docs.oracle.com/javase/6/docs/technotes/tools/findingclasses.html</a><br>
<code>[10].</code> <a href='http://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html'>http://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html</a><br>
<code>[11].</code> <a href='https://wikis.oracle.com/display/HotSpotInternals/CompressedOops'>https://wikis.oracle.com/display/HotSpotInternals/CompressedOops</a><br>
<code>[12].</code> <a href='http://www.slideshare.net/RednaxelaFX/usenuma-on-jdk6linux'>http://www.slideshare.net/RednaxelaFX/usenuma-on-jdk6linux</a><br>
<code>[13].</code> <a href='http://zh.wikipedia.org/wiki/%E9%9D%9E%E5%9D%87%E5%8C%80%E8%AE%BF%E5%AD%98%E6%A8%A1%E5%9E%8B'>http://zh.wikipedia.org/wiki/%E9%9D%9E%E5%9D%87%E5%8C%80%E8%AE%BF%E5%AD%98%E6%A8%A1%E5%9E%8B</a><br>
<code>[14].</code> <a href='http://www.oracle.com/technetwork/java/hotspotfaq-138619.html'>http://www.oracle.com/technetwork/java/hotspotfaq-138619.html</a><br>
<code>[15].</code> <a href='http://en.wikipedia.org/wiki/Escape_analysis'>http://en.wikipedia.org/wiki/Escape_analysis</a><br>
<code>[16].</code> <a href='http://kenwublog.com/jvm-optimization-escape-analysis'>http://kenwublog.com/jvm-optimization-escape-analysis</a><br>
<code>[17].</code> <a href='http://rednaxelafx.iteye.com/blog/659108'>http://rednaxelafx.iteye.com/blog/659108</a><br>
<code>[18].</code> <a href='http://kenwublog.com/compressedoops'>http://kenwublog.com/compressedoops</a><br>
<code>[19].</code> <a href='http://kenwublog.com/docs/java6-jvm-options-chinese-edition.htm'>http://kenwublog.com/docs/java6-jvm-options-chinese-edition.htm</a><br>
<code>[20].</code> <a href='http://www.tektalk.org/tektalk.org/wp-content/uploads/2011/05/Java_Program_in_Action_20110727.pdf'>http://www.tektalk.org/tektalk.org/wp-content/uploads/2011/05/Java_Program_in_Action_20110727.pdf</a><br>
<code>[21].</code> <a href='http://zzzoot.blogspot.com/2009/02/java-mysql-increased-performance-with.html'>http://zzzoot.blogspot.com/2009/02/java-mysql-increased-performance-with.html</a><br>
<code>[22].</code> <a href='http://www.oracle.com/technetwork/java/javase/tech/largememory-jsp-137182.html'>http://www.oracle.com/technetwork/java/javase/tech/largememory-jsp-137182.html</a><br>
<code>[23].</code> <a href='http://zh.wikipedia.org/wiki/%E8%BD%89%E8%AD%AF%E5%BE%8C%E5%82%99%E7%B7%A9%E8%A1%9D%E5%8D%80'>http://zh.wikipedia.org/wiki/%E8%BD%89%E8%AD%AF%E5%BE%8C%E5%82%99%E7%B7%A9%E8%A1%9D%E5%8D%80</a><br>
<code>[24].</code> <a href='http://kenwublog.com/tune-large-page-for-jvm-optimization'>http://kenwublog.com/tune-large-page-for-jvm-optimization</a><br>
<code>[25].</code> <a href='http://en.wikipedia.org/wiki/Streaming_SIMD_Extensions'>http://en.wikipedia.org/wiki/Streaming_SIMD_Extensions</a><br>
<code>[26].</code> <a href='http://zh.wikipedia.org/wiki/SSE'>http://zh.wikipedia.org/wiki/SSE</a><br>
<code>[27].</code> <a href='http://nothingcosmos.github.com/OpenJDKOverview/src/cpu.html'>http://nothingcosmos.github.com/OpenJDKOverview/src/cpu.html</a><br>
<code>[28].</code> <a href='http://home.scarlet.be/bernard.jorion5/prgs/books/mem-management.pdf'>http://home.scarlet.be/bernard.jorion5/prgs/books/mem-management.pdf</a><br>
<code>[29].</code> <a href='https://blogs.oracle.com/jonthecollector/entry/the_real_thing'>https://blogs.oracle.com/jonthecollector/entry/the_real_thing</a><br>
<code>[30].</code> <a href='http://hi.baidu.com/7636553/item/80f9bd144536e5cb39cb300a'>http://hi.baidu.com/7636553/item/80f9bd144536e5cb39cb300a</a><br>
<code>[31].</code> <a href='http://stackoverflow.com/questions/1120088/what-is-javas-xxusemembar-parameter'>http://stackoverflow.com/questions/1120088/what-is-javas-xxusemembar-parameter</a><br>
<code>[32].</code> <a href='http://www.open-open.com/home/space.php?uid=71669&do=blog&id=8891'>http://www.open-open.com/home/space.php?uid=71669&amp;do=blog&amp;id=8891</a><br>
<code>[33].</code> <a href='http://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html'>http://www.cnblogs.com/redcreen/archive/2011/05/04/2037057.html</a><br>
<code>[34].</code> <a href='http://blog.csdn.net/fengbaoxp/article/details/4330564'>http://blog.csdn.net/fengbaoxp/article/details/4330564</a><br>
<code>[35].</code> <a href='http://ig2net.info/archives/821.html'>http://ig2net.info/archives/821.html</a><br>