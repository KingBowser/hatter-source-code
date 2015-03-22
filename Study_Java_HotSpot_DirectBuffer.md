### 简介及其原理 ###

`DirectBuffer` 通过免去中间交换的内存拷贝, 提升IO处理速度;也就是常说的zero-copy

  * `ByteBuffer`
    * `HeapByteBuffer`
    * `MappedByteBuffer`
      * `MappedByteBuffer.map(int mode,long position,long size); `
| `READ_ONLY` | 只读 | 试图修改得到的缓冲区将导致抛出 ReadOnlyBufferException |
|:------------|:-------|:----------------------------------------------------------------------|
| `READ_WRITE` | 读/写 | 对得到的缓冲区的更改最终将传播到文件；该更改对映射到同一文件的其他程序不一定是可见的 |
| `PRIVATE` | 专用 | 对得到的缓冲区的更改不会传播到文件，并且该更改对映射到同一文件的其他程序也不是可见的；相反，会创建缓冲区已修改部分的专用副本 |

| **分配** | `java.nio.ByteBuffer.allocateDirect` |
|:-----------|:-------------------------------------|
| **释放** | `sun.nio.ch.DirectBuffer.cleaner().clean()` |

可以通过参数 `-XX:MaxDirectMemorySize=` 设置其可使用的最大内存大小，当未设置时默认同 `-Xmx` 的大小。

### 各种应用 ###
| `BigMemory` | http://terracotta.org/products/bigmemory | BigMemory stores “big” amounts of data in machine memory for ultra-fast access |
|:------------|:-----------------------------------------|:-----------------------------------------------------------------------------------|
| `DirectMemory` | http://incubator.apache.org/directmemory/ | Apache DirectMemory is a multi layered cache implementation featuring off-heap memory management (a-la BigMemory) to enable efficient handling of a large number of java objects without affecting jvm garbage collection performance |
| `HugeCollections` | https://code.google.com/p/vanilla-java/wiki/HugeCollections | If you want to efficiently store large collections of data in memory. This library can dramatically reduce Full GC times and reduce memory consumption as well |

### OutOfMemoryError ###
和 `DirectBuffer` 相关的内存泄漏参看[Study\_Java\_HotSpot\_OOME](Study_Java_HotSpot_OOME.md)

### 参考资料 ###
`[1].` http://blog.csdn.net/fancyerii/article/details/7655451<br>
<code>[2].</code> <a href='http://lixjluck.iteye.com/blog/1130455'>http://lixjluck.iteye.com/blog/1130455</a><br>
<code>[3].</code> <a href='http://www.zhurouyoudu.com/index.php/archives/470/'>http://www.zhurouyoudu.com/index.php/archives/470/</a><br>