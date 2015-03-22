Sockets Direct Protocol（SDP）最初由Infiniband行业协会（InfiniBand Trade Association）的软件工作组（Software Working Group）所指定，主要针对Infiniband架构。后来SDP发展成为利用RDMA特性进行传输的重要协议，并被推广到iWARP等网络上。SDP利用RDMA网络特性能够高效的进行零拷贝的数据传输。SDP协议的设计目标是为了使得应用程序能够透明地利用RDMA通信机制来加速传统的TCP/IP网络通信。

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/network/RDMA/Figure1.jpg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/network/RDMA/Figure1.jpg)

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/network/RDMA/TraditionalDMA.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/network/RDMA/TraditionalDMA.png)

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/network/RDMA/RemoteDMA.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/network/RDMA/RemoteDMA.png)


### 参考资料 ###
`[1].` http://zh.wikipedia.org/wiki/Sockets_Direct_Protocol<br>
<code>[2].</code> <a href='http://en.wikipedia.org/wiki/Remote_direct_memory_access'>http://en.wikipedia.org/wiki/Remote_direct_memory_access</a><br>
<code>[3].</code> <a href='http://hi.baidu.com/widebright/item/54a035f658312848932af2af'>http://hi.baidu.com/widebright/item/54a035f658312848932af2af</a><br>
<code>[4].</code> <a href='http://www.cs.albany.edu/~jhh/courses/readings/frey.icdcs09.rdma.pdf'>http://www.cs.albany.edu/~jhh/courses/readings/frey.icdcs09.rdma.pdf</a><br>
<code>[5].</code> <a href='http://www.systems.ethz.ch/sites/default/files/file/Spring2013_Courses/AdvCompNetw_Spring2013/11-Network-Virtualization-1up.pdf'>http://www.systems.ethz.ch/sites/default/files/file/Spring2013_Courses/AdvCompNetw_Spring2013/11-Network-Virtualization-1up.pdf</a><br>
<code>[6].</code> <a href='http://www.ietf.org/rfc/rfc4297.txt'>http://www.ietf.org/rfc/rfc4297.txt</a><br>