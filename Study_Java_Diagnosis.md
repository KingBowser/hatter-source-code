# Hatter的诊断工具 #

| **Name** | **URL** | **License** | **Category** | **Description** |
|:---------|:--------|:------------|:-------------|:----------------|
| [finding](finding.md) | https://hatter-source-code.googlecode.com/svn/trunk/finding/ | `New_BSD` | `Java Tool` | Find in source code |
| [invokefind](invokefind.md) | https://hatter-source-code.googlecode.com/svn/trunk/invokefind/ | `New_BSD` | `Java Tool` | invoke find |
| libana | https://hatter-source-code.googlecode.com/svn/trunk/libana/ | `New_BSD` | `Java Tool` | lib analysis |
| [jtop](jtop.md) | https://hatter-source-code.googlecode.com/svn/trunk/jtop/ | `New_BSD` | `HotSpot Tool` | Java edition of `top` |
| [histodiff](histodiff.md) | https://hatter-source-code.googlecode.com/svn/trunk/histoana/ | `GPL` | `HotSpot Tool` | Diff of `jmap -histo` |
| [permstat](permstat.md) | https://hatter-source-code.googlecode.com/svn/trunk/permstat/ | `GPL` | `HotSpot Tool` | Diff of perm string constants |
| [hotstat](hotstat.md) | https://hatter-source-code.googlecode.com/svn/trunk/hotstat/ | `GPL` | `HotSpot Tool` | Diff of HotSpot JVM monitors |
| [jflag](jflag.md) | https://hatter-source-code.googlecode.com/svn/trunk/jflag/ | `GPL` | `HotSpot Tool` | Show/Set HotSpot JVM flags |
| [jprop](jprop.md) | https://hatter-source-code.googlecode.com/svn/trunk/jprop/ | `GPL` | `HotSpot Tool` | Show/Set HotSpot system/agent properties |
| [classlist](classlist.md) | https://hatter-source-code.googlecode.com/svn/trunk/classlist/ | `GPL` | `HotSpot Tool` | Show class stat in perm |
| [classdump](classdump.md) | https://hatter-source-code.googlecode.com/svn/trunk/classdump/ | `GPL` | `HotSpot Tool` | Dump class in hotspot jvm |
| [directbufferana](directbufferana.md) | https://hatter-source-code.googlecode.com/svn/trunk/directbufferana/ | `GPL` | `HotSpot Tool` | Analysis Direct Buffer Size |

# 第三方诊断工具 #
| **Name** | **URL** | **Description** |
|:---------|:--------|:----------------|
| [BTrace](BTrace.md) | http://kenai.com/projects/btrace | BTrace is a safe, dynamic tracing tool for the Java platform. BTrace can be used to dynamically trace a running Java program (similar to DTrace for OpenSolaris applications and OS). |
| CRaSH | http://www.crashub.org/ | A shell to extend the Java Platform, Open source and open minded. |
| TProfiler | https://github.com/taobao/TProfiler | Profiler is a code profiling tool. |
|  HouseMD | https://github.com/CSUG/HouseMD | HouseMD is a interactive command-line tool for dianosing Java process in runtime. It's inspiration came from BTrace, but more easier to use and more safer. |
| gperftools | https://code.google.com/p/gperftools/ | These tools are for use by developers so that they can create more robust applications. Especially of use to those developing multi-threaded applications in C++ with templates. Includes TCMalloc, heap-checker, heap-profiler and cpu-profiler. |
| Valgrind | http://valgrind.org/ | Valgrind is an instrumentation framework for building dynamic analysis tools. |
| gcprof | https://github.com/twitter/jvmgcprof | gcprof is a simple utility for profile allocation and garbage collection activity in the JVM |
| Classmexer | http://www.javamex.com/classmexer/ | Classmexer is a simple Java instrumentation agent that provides some convenience calls for measuring the memory usage of Java objects from within an application. |
| Jmxterm | http://wiki.cyclopsgroup.org/jmxterm | Jmxterm is a command line based interactive JMX client. |
| TBJMap | https://github.com/jlusdy/TBJMap | 基于Serviceability Agent,对JMap做了增强,可以方便的输出JVM堆中,每一个分区的对象实例个数和大小的Histogram图. |
| Aspersa | https://code.google.com/p/aspersa/| Aspersa has become part of Percona Toolkit, and there will be no further development or releases separate from Percona Toolkit. |
| Heapster | https://github.com/mariusaeriksen/heapster | Heapster provides an agent library to do heap profiling for JVM processes with output compatible with Google perftools. |
| OProfile  | http://oprofile.sourceforge.net/ | OProfile is a system-wide profiler for Linux systems, capable of profiling all running code at low overhead. |
| java.sizeOf | http://sizeof.sourceforge.net/ | With java.SizeOf you can measure the real memory size of your Java objects. |
| b-profiler | https://code.google.com/p/b-profiler/ | 用BTrace写的一个Profiler |
| GCView | http://www.tagtraum.com/gcviewer.html | GCViewer is a free open source tool to visualize data produced by the Java VM options `-verbose:gc` and `-Xloggc:<file>` |
| GCStats | http://www.performize-it.com/tools/gcstats | This jar contains three tools which can help u understand GC behavior |
| Thread Top | http://www.performize-it.com/tools/thread-top | ThreadTop is a simple command line tool which uses JMX protocol to connect to a Java process |
| Greys Anatomy | https://code.google.com/p/greys-anatomy/<br> <a href='https://github.com/chengtongda/greys-anatomy'>https://github.com/chengtongda/greys-anatomy</a> <table><thead><th> greys-anatomy 是一个java进程执行过程中的异常诊断工具 </th></thead><tbody>
<tr><td> JIP </td><td> <a href='http://jiprof.sourceforge.net/'>http://jiprof.sourceforge.net/</a> </td><td> JIP — The Java Interactive Profiler </td></tr>
<tr><td> Diagnostic Tools </td><td> <a href='http://clarkgrubb.com/diagnostic-tools'>http://clarkgrubb.com/diagnostic-tools</a> </td><td> Diagnostic Tools: Linux, Mac OS X, Windows </td></tr></tbody></table>

<h1>相关资讯</h1>
<ul><li><a href='http://www.ibm.com/developerworks/cn/linux/l-cn-perf1/index.html'>http://www.ibm.com/developerworks/cn/linux/l-cn-perf1/index.html</a>