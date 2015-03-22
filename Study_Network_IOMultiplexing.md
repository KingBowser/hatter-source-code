IO复用的几个具体技术
  * select
  * poll
  * event-based
    * epoll (Linux)  - 查看支持最大FD数 `cat /proc/sys/fs/file-max`
    * kqueue (FreeBSD)
    * ...

| **-** | **select** | **poll** | **epoll** |
|:------|:-----------|:---------|:----------|
| **简介** | select本质上是通过设置或者检查存放fd标志位的数据结构来进行下一步处理。这样所带来的缺点是：<br>1 单个进程可监视的fd数量被限制<br>2 需要维护一个用来存放大量fd的数据结构，这样会使得用户空间和内核空间在传递该结构时复制开销大<br>3 对socket进行扫描时是线性扫描 <table><thead><th> poll本质上和select没有区别，它将用户传入的数组拷贝到内核空间，然后查询每个fd对应的设备状态，如果设备就绪则在设备等待队列中加入一项并继续遍历，如果遍历完所有fd后没有发现就绪设备，则挂起当前进程，直到设备就绪或者主动超时，被唤醒后它又要再次遍历fd。这个过程经历了多次无谓的遍历。<br>它没有最大连接数的限制，原因是它是基于链表来存储的，但是同样有一个缺点：<br>大量的fd的数组被整体复制于用户态和内核地址空间之间，而不管这样的复制是不是有意义。<br>poll还有一个特点是“水平触发”，如果报告了fd后，没有被处理，那么下次poll时会再次报告该fd。</th><th> epoll支持水平触发和边缘触发，最大的特点在于边缘触发，它只告诉进程哪些fd刚刚变为就需态，并且只会通知一次。<br>在前面说到的复制问题上，epoll使用mmap减少复制开销。<br>还有一个特点是，epoll使用“事件”的就绪通知方式，通过epoll_ctl注册fd，一旦该fd就绪，内核就会采用类似callback的回调机制来激活该fd，epoll_wait便可以收到通知 </th></thead><tbody>
<tr><td> <b>支持一个进程所能打开的最大连接数</b> </td><td> 单个进程所能打开的最大连接数有FD_SETSIZE宏定义，其大小是32个整数的大小（在32位的机器上，大小就是32*32，同理64位机器上FD_SETSIZE为32*64），当然我们可以对进行修改，然后重新编译内核，但是性能可能会受到影响，这需要进一步的测试。 </td><td> poll本质上和select没有区别，但是它没有最大连接数的限制，原因是它是基于链表来存储的 </td><td> 虽然连接数有上限，但是很大，1G内存的机器上可以打开10万左右的连接，2G内存的机器可以打开20万左右的连接 </td></tr>
<tr><td> <b>FD剧增后带来的IO效率问题</b> </td><td> 因为每次调用时都会对连接进行线性遍历，所以随着FD的增加会造成遍历速度慢的“线性下降性能问题”。 </td><td> 同 select </td><td> 因为epoll内核中实现是根据每个fd上的callback函数来实现的，只有活跃的socket才会主动调用callback，所以在活跃 socket较少的情况下，使用epoll没有前面两者的线性下降的性能问题，但是所有socket都很活跃的情况下，可能会有性能问题。 </td></tr>
<tr><td> <b>消息传递方式</b> </td><td> 内核需要将消息传递到用户空间，都需要内核拷贝动作 </td><td> 同 select </td><td> epoll通过内核和用户空间共享一块内存来实现的。 </td></tr></tbody></table>




<br>

Related: <a href='Study_OS_Linux_IO.md'>Study_OS_Linux_IO</a>

<h3>参考资料</h3>
<code>[1].</code> <a href='http://daniel.haxx.se/docs/poll-vs-select.html'>http://daniel.haxx.se/docs/poll-vs-select.html</a><br>
<code>[2].</code> <a href='http://www.cnblogs.com/bigwangdi/p/3182958.html'>http://www.cnblogs.com/bigwangdi/p/3182958.html</a><br>
<code>[3].</code> <a href='http://my.oschina.net/zhangjie830621/blog/201386'>http://my.oschina.net/zhangjie830621/blog/201386</a><br>