
```
jcmd <pid> <command> ...

Sample:
$ jcmd

$ jcmd <pid> help

$ jcmd <pid | main class> <command | PerfCounter.print | -f filename>
```


## jcmd命令列表： ##
| **命令名** | **成本** | **说明** |
|:--------------|:-----------|:-----------|
| `help` | `N/A` | 帮助 |
| `ManagementAgent.stop` |  |  |
| `ManagementAgent.start_local` |  |  |
| `ManagementAgent.start` |  |  |
| `Thread.print` |  |  |
| `GC.class_histogram` |  |  |
| `GC.heap_dump` |  |  |
| `GC.run_finalization` |  |  |
| `GC.run` |  |  |
| `VM.uptime` |  |  |
| `VM.flags` |  |  |
| `VM.system_properties` |  |  |
| `VM.command_line` |  |  |
| `VM.version` |  |  |
| `VM.commercial_features` |  |  |
| `JFR.stop` |  |  |
| `JFR.start` |  |  |
| `JFR.dump` |  |  |
| `JFR.check` |  |  |


### 参考资料 ###
`[1].` http://www.coppermine.jp/docs/programming/2012/08/jdk7u4.html<br>
<code>[2].</code> <a href='http://www.slideshare.net/TsunenagaHanyuda/jcmd-16803399'>http://www.slideshare.net/TsunenagaHanyuda/jcmd-16803399</a><br>