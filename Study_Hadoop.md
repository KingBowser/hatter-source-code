<sup>[注]</sup> 值得注意的是Hadoop集群的所有机器的配置应该保持一致，一般我们在配置完master后，使用scp将配置文件同步到集群的其它服务器上。


```
./hadoop namenode -format
./start-all.sh

hadoop fs -ls /
```

```
http://namenode:50070
http://jobtracker:50030
```



### 参考资料 ###
`[1].` http://yymmiinngg.iteye.com/blog/706699<br>