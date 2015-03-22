# 为什么删除了主库BINLOG，同步依然正常？ #

## 测试场景如下： ##
1、搭建主备复制<br>
2、在主库使用sysbench加载压力，多个进程同时加载数据到多个表；<br>
3、删除主库的二进制文件；<br>
<br>
<h2>结果：</h2>
1、同步正常；<br>
2、备库数据完整；<br>
3、重启主库，因为找不到二进制文件而报错不能正常重启；<br>
<br>
<h2>问题：</h2>
用于同步的二进制文件都删除了，为啥仍然得到这个结果？备库IO线程读取的是主库操作系统缓存或者文件系统缓存的数据么？<br>
<br>
<h2>回答：</h2>
备库的IO线程其实不是拉数据，而是MySQL的master上面专门的binlog dump线程来推数据的。<br>
每次写binlog的时候，binlog dump线程都会将对应的event信息发给备库。<br>
在linux操作系统上，如果一个进程打开了binlog文件，只要这个进程没有关闭文件，那么就算你在另外的窗口删除掉了这个文件，这个文件此时并不会真正的删除。<br>
如果mysqld进程打开了binlog文件，就算你删除了，mysqld仍然可以写数据进去(用lsof仍然可以看到mysqld进程打开了那个binlog文件)，直到mysqld完成这个binlog，切换到下一个，这个binlog才真正的被物理删除。所以在此期间，binlog dump线程一直可以把binlog的events发给备库，备库也一直能够保持跟主库的同步。<br>
<br>
<br>
<h3>参考资料</h3>
<code>[1].</code> <a href='http://www.woqutech.com/?p=812'>http://www.woqutech.com/?p=812</a><br>