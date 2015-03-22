Linux有两个重要的内核参数与TCP超时重传相关：`/proc/sys/net/ipv4/tcp_retries1`和`/proc/sys/net/ipv4/tcp_retries2`。前者指定在底层IP接管之前TCP最少执行的重传次数，默认值是3。后者指定连接放弃前TCP最多可以执行的重传次数，默认值是15（一般对应13～30?min）。在我们的实例中，TCP超时重传发生了5次，连接坚持的时间是15?min（可以用date命令来测量）。


TCP拥塞控制的标准文档是RFC 5681，其中详细介绍了拥塞控制的四个部分：慢启动（slow start）、拥塞避免（congestion avoidance）、快速重传（fast retransmit）和快速恢复（fast recovery）。拥塞控制算法在Linux下有多种实现，比如reno算法、vegas算法和cubic算法等。它们或者部分或者全部实现了上述四个部分。`/proc/sys/net/ipv4/tcp_congestion_control`文件指示机器当前所使用的拥塞控制算法。



### 参考资料 ###
`[1].` http://book.51cto.com/art/201306/400277.htm<br>
<code>[2].</code> <a href='http://book.51cto.com/art/201306/400278.htm'>http://book.51cto.com/art/201306/400278.htm</a><br>
<code>[3].</code> <a href='http://www.oschina.net/question/17_31076'>http://www.oschina.net/question/17_31076</a><br>