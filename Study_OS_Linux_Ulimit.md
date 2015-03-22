## 配置文件 ##
```
/etc/security/limits.conf

/etc/security/limits.d/90-nproc.conf
```

```
cat /proc/<pid>/limits
```

## ulimit 参数说明 ##
| 选项 [options](options.md) | 含义 | 例子 |
|:-----------------------------|:-------|:-------|
| -H	 | 设置硬资源限制，一旦设置不能增加。 |  ulimit -Hs 64；限制硬资源，线程栈大小为 64K。 |
| -S | 设置软资源限制，设置后可以增加，但是不能超过硬资源设置。 |  ulimit -Sn 32；限制软资源，32 个文件描述符。 |
| -a | 显示当前所有的 limit 信息。 |  ulimit -a；显示当前所有的 limit 信息。 |
| -c | 最大的 core 文件的大小， 以 blocks 为单位。 |  ulimit -c unlimited； 对生成的 core 文件的大小不进行限制。 |
| -d | 进程最大的数据段的大小，以 Kbytes 为单位。 |  ulimit -d unlimited；对进程的数据段大小不进行限制。 |
| -f | 进程可以创建文件的最大值，以 blocks 为单位。 |  ulimit -f 2048；限制进程可以创建的最大文件大小为 2048 blocks。 |
| -l | 最大可加锁内存大小，以 Kbytes 为单位。 |  ulimit -l 32；限制最大可加锁内存大小为 32 Kbytes。 |
| -m | 最大内存大小，以 Kbytes 为单位。 |  ulimit -m unlimited；对最大内存不进行限制。 |
| -n | 可以打开最大文件描述符的数量。 |  ulimit -n 128；限制最大可以使用 128 个文件描述符。 |
| -p | 管道缓冲区的大小，以 Kbytes 为单位。 |  ulimit -p 512；限制管道缓冲区的大小为 512 Kbytes。 |
| -s | 线程栈大小，以 Kbytes 为单位。 |  ulimit -s 512；限制线程栈的大小为 512 Kbytes。 |
| -t | 最大的 CPU 占用时间，以秒为单位。 |  ulimit -t unlimited；对最大的 CPU 占用时间不进行限制。 |
| -u | 用户最大可用的进程数。 |  ulimit -u 64；限制用户最多可以使用 64 个进程。 |
| -v | 进程最大可用的虚拟内存，以 Kbytes 为单位。 |  ulimit -v 200000；限制最大可用的虚拟内存为 200000 Kbytes。  |


### 参考资料 ###
`[1].` http://www.ibm.com/developerworks/cn/linux/l-cn-ulimit/<br>