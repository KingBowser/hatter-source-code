Client Subnet in DNS Requests draft-vandergaast-edns-client-subnet-02 - http://tools.ietf.org/html/draft-vandergaast-edns-client-subnet-02


![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/dns/edns-client-subnet1.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/dns/edns-client-subnet1.png)


## [DNS Rebinding Attacks](http://crypto.stanford.edu/dns/) ##

# [What's My DNS](https://www.whatsmydns.net/) #
# [Global DNS Checker](http://www.nexcess.net/resources/tools/global-dns-checker/) #

# [PUBLIC SUFFIX LIST](https://publicsuffix.org/) #

<br>


查看DNS记录的几种方式：<br>
<pre><code>nslookup -q=TXT &lt;DOMAIN&gt;<br>
dig +short txt &lt;DOMAIN&gt;<br>
host -t TXT &lt;DOMAIN&gt;<br>
</code></pre>

<br>


为什么根域名服务器是13个而不是更多？<br>
<br>
因为希望把对根域名查询的响应包控制在512字节以下。如果响应包超过512字节，服务器会让客户端使用TCP重新查询。同时RFC791规定，路由器至少要能处理的MTU是576字节，除去20字节IP头，8字节UDP头和可能的IP选项，payload为512字节可以确保这个UDP包不被IP层分片。<br>
<br>
<br>
<h3>参考资料</h3>
<code>[1].</code> <a href='http://noops.me/?p=653'>http://noops.me/?p=653</a><br>
<code>[2].</code> <a href='http://archercai.blog.sohu.com/60779796.html'>http://archercai.blog.sohu.com/60779796.html</a><br>
<code>[3].</code> <a href='https://code.google.com/p/nevel-jupiter/wiki/DNS'>https://code.google.com/p/nevel-jupiter/wiki/DNS</a><br>