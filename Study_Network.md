影响TCP性能的协议因素：<sup>[1]</sup>
```
（1）TCP连接建立握手
（2）TCP慢启动拥塞控制；
（3）数据聚集的Nagle算法；
（4）用于捎带确认的TCP延迟确认机制。
（5）TIME_WAIT时延和端口耗尽。
1、2可通过长连接避免，3可使用TCP_NODELAY避免，4可通过调整内核栈参数避免，但调整需谨慎，5通常只在性能测试环境出现。
```

网卡流量统计：
```
#!/bin/bash
alias ifconfig="/sbin/ifconfig"
eth=eth0
while true; do
RXpre=$(ifconfig ${eth} | grep bytes | awk '{print $2}'| awk -F":" '{print $2}')
TXpre=$(ifconfig ${eth} | grep bytes | awk '{print $6}' | awk -F":" '{print $2}')
sleep 1
RXnext=$(ifconfig ${eth} | grep bytes | awk '{print $2}'| awk -F":" '{print $2}')
TXnext=$(ifconfig ${eth} | grep bytes | awk '{print $6}' | awk -F":" '{print $2}')
echo RX ----- TX
echo "$((((${RXnext}-${RXpre})/1024)/1024))MB/s $((((${TXnext}-${TXpre})/1024/1024)))MB/s"
done
```

CSNA: http://www.csna.cn/

[TCP/IP](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/network/tcpip.gif)
[网络通讯协议图](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/network/network-protocol-map-2013.pdf)
[网络应用故障分析表](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/network/troubleshooting.pdf)



_SEE ALSO: [Study\_OS\_Linux\_Network](Study_OS_Linux_Network.md) [Study\_OS\_Linux\_Network](Study_OS_Linux_Network.md)_


### 参考资料 ###
`[1].` http://weibo.com/2218694820/yFOs9jMKq<br>