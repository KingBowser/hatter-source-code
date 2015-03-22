## 时间更新 ##

  * 指定时间服务器时间同步

```
rdate -s time.nist.gov
```

-OR-

```
yum install -y ntpdate

/usr/sbin/ntpdate us.pool.ntp.org
```


  * NTP

```
配置文件： /etc/ntp.conf

service ntpd start
```


<br>
Related: <a href='Study_Time.md'>Study_Time</a>


<h3>参考资料</h3>
<code>[1].</code> <a href='http://blueicer.blog.51cto.com/395686/116426'>http://blueicer.blog.51cto.com/395686/116426</a><br>
<code>[2].</code> <a href='http://os.51cto.com/art/201004/192549.htm'>http://os.51cto.com/art/201004/192549.htm</a><br>