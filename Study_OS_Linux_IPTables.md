**iptables**

```
service iptables start
service iptables restart
service iptables stop
```

```
/etc/sysconfig/iptables
iptables --list
```

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/iptables/iptables_linux_arch.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/iptables/iptables_linux_arch.png)

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/iptables/iptables_stream.jpg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/iptables/iptables_stream.jpg)

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/iptables/iptables_command.png](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/iptables/iptables_command.png)


| **参数** | **说明** |
|:-----------|:-----------|
| `-A` | 指定链名   |
| `-p` | 指定协议类型   |
| `-d` | 指定目标地址   |
| `--dport` | 指定目标端口（destination port 目的端口）   |
| `--sport` | 指定源端口（source port 源端口）   |
| `-j` | 指定动作类型  |

**保存规则:**
```
/etc/init.d/iptables save
```

**如果删除规则:**
```
iptables -L INPUT --line-numbers
^---- 列出规则

iptables -D INPUT 4
^---- 删除规则
```

**IP屏蔽:**
```
#屏蔽单个IP的命令是  
iptables -I INPUT -s 123.45.6.7 -j DROP  
#封整个段即从123.0.0.1到123.255.255.254的命令  
iptables -I INPUT -s 123.0.0.0/8 -j DROP  
#封IP段即从123.45.0.1到123.45.255.254的命令  
iptables -I INPUT -s 124.45.0.0/16 -j DROP  
#封IP段即从123.45.6.1到123.45.6.254的命令是  
iptables -I INPUT -s 123.45.6.0/24 -j DROP  

指令I是insert指令 但是该指令会insert在正确位置并不像A指令看你自己的排序位置，因此用屏蔽因为必须在一开始就要加载屏蔽IP，
所以必须使用I命令加载，然后注意执行/etc/rc.d/init.d/iptables save进行保存后重启服务即可
```

### 参考资料 ###
`[1].` http://jsczxy2.iteye.com/blog/1436713<br>
<code>[2].</code> <a href='http://www.kuqin.com/shuoit/20150226/344944.html'>http://www.kuqin.com/shuoit/20150226/344944.html</a><br>
<code>[3].</code> <a href='http://seanlook.com/2014/02/26/iptables-example/'>http://seanlook.com/2014/02/26/iptables-example/</a><br>