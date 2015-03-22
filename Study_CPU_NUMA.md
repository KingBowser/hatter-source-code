非均匀访存模型（NUMA Non-Uniform Memory Access）的特点是：被共享的存储器物理上是分布式的，所有这些存储器的集合就是全局地址空间。所以处理器访问这些存储器的时间是不一样的，显然访问本地存储器的速度要比访问全局共享存储器或远程访问外地存储器要快些。另外，NUMA中存储器可能是分层的：本地存储器，群内共享存储器，全局共享存储器。 <sup>[2]</sup>

下图是Intel 5600 CPU的架构：<sup>[3]</sup>
![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/x86/intl_numa.jpg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/x86/intl_numa.jpg)

Intel QuickPath Interconnect (Intel QPI)技术 <sup>[5]</sup>
> 使用QPI架构代替了原来的FSB架构，QPI是基于数据包传输，高带宽低延迟的点到点传输技术，速度可以达到6.4GT/s，对双向传输的QPI总线连接来说理论最大值可以达到25.6GB/s的数据传输，远远高于原来基于FSB架构的数据带宽。

NUMA的内存分配策略有四种： <sup>[3]</sup>
  1. 缺省(default)：总是在本地节点分配（分配在当前进程运行的节点上）；
  1. 绑定(bind)：强制分配到指定节点上；
  1. 交叉(interleave)：在所有节点或者指定的节点上交织分配；
  1. 优先(preferred)：在指定节点上分配，失败则在其他节点上分配。

> 因为NUMA默认的内存分配策略是优先在进程所在CPU的本地内存中分配，会导致CPU节点之间内存分配不均衡，当某个CPU节点的内存不足时，会导致swap产生，而不是从远程节点分配内存。这就是所谓的swap insanity现象。

### 参考资料 ###
`[1].` http://en.wikipedia.org/wiki/Non-Uniform_Memory_Access<br>
<code>[2].</code> <a href='http://zh.wikipedia.org/wiki/%E9%9D%9E%E5%9D%87%E5%8C%80%E8%AE%BF%E5%AD%98%E6%A8%A1%E5%9E%8B'>http://zh.wikipedia.org/wiki/%E9%9D%9E%E5%9D%87%E5%8C%80%E8%AE%BF%E5%AD%98%E6%A8%A1%E5%9E%8B</a><br>
<code>[3].</code> <a href='http://www.hellodb.net/2011/06/mysql_multi_instance.html'>http://www.hellodb.net/2011/06/mysql_multi_instance.html</a><br>
<code>[4].</code> <a href='http://software.intel.com/zh-cn/blogs/2008/11/24/numaxeon1/'>http://software.intel.com/zh-cn/blogs/2008/11/24/numaxeon1/</a><br>
<code>[5].</code> <a href='http://software.intel.com/zh-cn/blogs/2008/11/28/numaxeon2/'>http://software.intel.com/zh-cn/blogs/2008/11/28/numaxeon2/</a><br>