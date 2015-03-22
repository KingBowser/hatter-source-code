# 配置大页 #

## 配置sysctl.conf ##
```
# vi /etc/sysctl.conf
kernel.shmmax = <share memory max in bytes>
vm.nr_hugepages = <huge page count>

# sysctl -p
```

## 写Linux内存文件 ##
```
# echo <share memory max in bytes> > /proc/sys/kernel/shmmax

# echo <huge page count> > /proc/sys/vm/nr_hugepages
```


### 参考资料 ###
`[1].` http://marongbo1982.blog.163.com/blog/static/355310012007999450842/<br>
<code>[2].</code> <a href='http://blog.csdn.net/miyao16/article/details/4909307'>http://blog.csdn.net/miyao16/article/details/4909307</a><br>