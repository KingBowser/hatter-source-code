# HotSpot #
`HotSpot` 是[Sun](http://en.wikipedia.org/wiki/Sun_Microsystems)(即现在的[Oracle](http://www.oracle.com/))官方JDK的虚拟机，也是[OpenJDK](http://openjdk.java.net/)的虚拟机。

## [OpenJDK JVM Internals](http://www.progdoc.de/papers/Jax2012/jax2012.html#(1)) ##

## [Hotspot Runtime Overview](http://openjdk.java.net/groups/hotspot/docs/RuntimeOverview.html) ##

## [HotSpot Collections](HotSpot_Profiler.md) ##

  * Check out HotSpot:
```
hg clone http://hg.openjdk.java.net/jdk7/hotspot/hotspot
```
  * Launcher
```
hotspot/src/os/linux/launcher/java.c
```
  * 打开JavaLauncher的Verbose信息：
```
export _JAVA_LAUNCHER_DEBUG=1
```
  * Download FastDebug HotSpot JDK `site:download.java.net fastdebug`
| JDK6 | http://download.java.net/jdk6/6u25/promoted/b03/index.html |
|:-----|:-----------------------------------------------------------|
| JDK7 | http://download.java.net/jdk7/archive/b130/ |

  * `Basic disassembler plugin for HotSpot`
> http://kenai.com/projects/base-hsdis


### 参考资料 ###
`[1].` http://openjdk.java.net/groups/hotspot/<br>
<code>[2].</code> <a href='http://java.sun.com/products/hotspot/docs/whitepaper/Java_HotSpot_WP_Final_4_30_01.html'>http://java.sun.com/products/hotspot/docs/whitepaper/Java_HotSpot_WP_Final_4_30_01.html</a><br>
<code>[3].</code> <a href='http://www.oracle.com/technetwork/java/javase/tech/index-jsp-136373.html'>http://www.oracle.com/technetwork/java/javase/tech/index-jsp-136373.html</a><br>