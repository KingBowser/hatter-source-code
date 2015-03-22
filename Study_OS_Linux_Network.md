| **Command** | **Description** |
|:------------|:----------------|
| `netstat -tpln` | 检查本地监听端口 |
| `netstat -tpn | grep ESTABLISHED` | 检查已建立的连接信息 |
| `netstat -i` | 查看丢包，网络是否繁忙，错误包是否严重 |


| **Parameter** | **Description** |
|:--------------|:----------------|
| `t` | tcp |
| `p` | 显示进程 |
| `l` | 处于监听的端口 |
| `n` | 数字显示ip、端口 |

<br>

<code>/etc/services</code> 文件存放：端口和服务(字符串端口)的对应关系<br>
<br>
<br>


<i>SEE ALSO: <a href='Study_Network.md'>Study_Network</a> <a href='Study_Java_HotSpot_Network.md'>Study_Java_HotSpot_Network</a></i>


<h3>参考资料</h3>
<code>[1].</code> <a href='http://blog.163.com/xychenbaihu@yeah/blog/static/132229655201222502510543'>http://blog.163.com/xychenbaihu@yeah/blog/static/132229655201222502510543</a><br>