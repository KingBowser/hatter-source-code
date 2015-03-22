
```
usage: vmstat [-V] [-n] [delay [count]]
              -V prints version.
              -n causes the headers not to be reprinted regularly.
              -a print inactive/active page stats.
              -d prints disk statistics
              -D prints disk table
              -p prints disk partition statistics
              -s prints vm table
              -m prints slabinfo
              -S unit size
              delay is the delay between updates in seconds. 
              unit size k:1000 K:1024 m:1000000 M:1048576 (default is K)
              count is the number of updates.
```

#### procs, 下面三种状态的进程数 ####
  * r 等待运行的进程数
  * b 在等待的进程数(通常在等待 IO)
  * w 可进入运行队列但被替换的进程(Solaris vmstat)

#### memory, 虚拟内存和实存信息 ####
  * swpd 虚拟内存使用情况, unit:kb
  * free 空闲的内存, unit:kb
  * buff 被用来做 buffer 的内存数, unit:kb
  * cache 被用来做 cache 的内存数, unit:kb
  * inactive 闲置的内存,unit:kb, -a显示
  * active 活动内存, unit:kb,-a显示

#### swap, 显示磁盘与内存每秒的交换 ####
  * si 从磁盘交换到内存的交换页数量, unit:kb
  * so 从内存交换到磁盘的交换页数量, unit:kb

#### io, 显示块设备每秒发送和收到的块数 ####
  * bi 发送到块设备的块数, unit: blocks
  * bo 从块设备接收到的数据, unit:blocks

#### system ####
  * in 每秒的中断数, 包括时钟中断
  * cs 每秒的环境切换次数

#### CPU (在多处理器系统上, 这是全部处理器的平均值) ####
  * us CPU 使用时间(except kernel)
  * sy CPU 系统使用时间(内核)
  * id 闲置时间
  * wa 等待 IO 的时间
  * st



---

```
通过 vmstat 识别 CPU 瓶颈

    r(运行队列)展示了正在执行和等待 CPU 资源的任务个数. 当这个值超过了 CPU 数目, 就会出现CPU瓶颈

    获得 CPU 个数的命令(Linux)：

[root@localhost ~]# cat /proc/cpuinfo | grep processor | wc -l
    当 r 值超过了CPU个数, 就会出现CPU 瓶颈, 解决办法大体如下

最简单的就是 增加 CPU个数
通过调整任务执行时间, 如大任务放到系统不繁忙的情况下进行执行, 进尔平衡系统任务
调整已有任务的优先级
通过 vmstat 识别 CPU 满负荷

    vmstat 中 CPU 的度量是百分比. 当us＋sy≈100时, 表示 CPU 正在接近满负荷工作. <br>
但要注意的是, CPU 满负荷工作并不能说明什么, *nix 总是试图要 CPU 尽可能的繁忙, <br>
使得任务的吞吐量最大化. 唯一能够确定 CPU 瓶颈的还是 r(运行队列) 的值.
```

### 参考资料 ###
`[1].` http://blogread.cn/it/article/3902<br>