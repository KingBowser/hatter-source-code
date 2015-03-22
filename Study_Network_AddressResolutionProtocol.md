# 地址解析协议 (Address Resolution Protocol) #

在IPv6中已不再适用，并被邻居发现协议（NDP）所替代

| **ARP请求包(ARP Request)** | 广播包(Broadcast) |
|:------------------------------|:---------------------|
| **ARP回复包(ARP Reply)** | 非广播包(Non-Broadcast) |



_广播包目的MAC地址为FF-FF-FF-FF-FF-FF，交换机设备接收到广播包后，会把它转发给局域网内的所有主机。_

### 参考资料 ###
`[1].` http://zh.wikipedia.org/wiki/%E5%9C%B0%E5%9D%80%E8%A7%A3%E6%9E%90%E5%8D%8F%E8%AE%AE<br>
<code>[2].</code> <a href='http://www.antiarp.com/about_73.html'>http://www.antiarp.com/about_73.html</a><br>