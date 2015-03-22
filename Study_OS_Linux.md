
```
$ which tail
/usr/bin/tail
$ dpkg -S /usr/bin/tail
coreutils: /usr/bin/tail
$ apt-get source coreutils
```

```
python -m SimpleHTTPServer
```

```
cat a b | sort | uniq > c   # c 是a和b的合集
cat a b | sort | uniq -d > c   # c 是a和b的交集
cat a b b | sort | uniq -u > c   # c 是a和b的不同

cat x | sort | uniq -c | sort -rn

汇总一个文本内容里第三列数字的和(这个方法要比用Python来做快3倍并只需1/3的代码量)：
awk '{ x += $3 } END { print x }' myfile
```


```
rsync -vrlHpgtSe ssh --exclude=.svn --delete /localdir/ name@host:/remotedir/
rsync -auvz --delete .. ..
```


```
wget --mirror --directory-prefix=<your directory> http://www.example.com
```

date
```
date +%Y%m%d
```

iconv
```
iconv -f GBK -t utf-8 a.txt > b.txt
```

Alias commands:
```
alias ll='ls -l'

alias j='java -jar'

alias vir='vi -R'

alias svnaddall='svn st | grep ? | awk '\''{print $2}'\'' | xargs svn add'
alias svnrmall='svn st | grep ! | awk '\''{print $2}'\'' | xargs svn rm'

alias mci='mvn clean install -Dmaven.test.skip'
alias mcp='mvn clean package -Dmaven.test.skip'
alias mct='mvn clean test'
alias mee='mvn eclipse:clean eclipse:eclipse'
```

for-in  http://www.cyberciti.biz/faq/bash-for-loop/
```
for i in {1..5}
do
   echo "Welcome $i times"
done

for i in {0..10..2}
  do
     echo "Welcome $i times"
 done

for i in $(seq 1 2 20)
do
   echo "Welcome $i times"
done

for (( EXP1; EXP2; EXP3 ))
do
	command1
	command2
	command3
done

for (( c=1; c<=5; c++ ))
do
   echo "Welcome $c times"
done

for OUTPUT in $(Linux-Or-Unix-Command-Here)
do
	command1 on $OUTPUT
	command2 on $OUTPUT
	commandN
done
```


X11 DISPLAY <sup>[1]</sup>
```
$ export DISPLAY=<ip>:0.0

$ xhost +
$ xhost +<ip>
```

gdb _pstack_
```
gdb -p pid

thread apply all bt


gdb \
    -ex "set pagination 0" \
    -ex "thread apply all bt" \
    --batch -p $(pidof mysqld)
```

ps
```
ps H -eo user,pid,ppid,tid,time,%cpu,cmd --sort=%cpu
```

```
tcpdump 命令常用参数说明 :
-i 网络接口
-w 保存文件名称
-s snaplen
-r 读取分析的文件名称
常用例子：tcpdump -i eth0 tcp port 8000 -s 0 -w cap.cap  
例子说明：
不加-s 0 参数数据包默认捕获数据信息不完整。
保存文件名称为.cap 后缀，可以使用开源分析工具wireshark 对抓到的数据包进行分析 ，wireshark 支持多种网络协议和分析方式。
```

查看是否丢包：
```
$netstat -s
```

Crontab:
> http://www.abunchofutils.com/utils/developer/cron-expression-helper/

Mac OS X:
```
curl -O http://ftp.gnu.org/gnu/wget/wget-1.13.4.tar.gz
```

打开命令的退出值:
```
cmd; echo $?
```

dstat
```
$ dstat -cdlmnpsy <SECONDS>
```

```
sar
top
iostat
vmstat
dstat
lsof
htop
netstat
ulimit
mpstat
pidstat

pstree -p <PID>

ping
nslookup
dig
whois

ldd xxx.so  查看依赖关系
nm 查看符号表信息
pmap 打印地址空间映射
pwdx 打开工作目录
pstack
strings
objdump
http://valgrind.org/docs/manual/manual.html
```

系统操作日志：
```
/var/log/sysadmin.log
```


普通用户以root权限跑程序：
```
rwsr-s--x root root

chgrp root _file_name_
chown root _file_name_
chmod 6751 _file_name_
```


```
sync  同步cache

释放cache
echo 3 > /proc/sys/vm/drop_caches
```


查看指定进程的环境变量：
```
tr \\0 \\n </proc/PID/environ

ps eww -p PID
```

```
/var/run/utmp -- database of currently logged-in users
/var/log/wtmp  -- database of past user logins
```


```
sed 's/aaa/bbb/g' file.name.txt 

// replace in files
sed -i 's/old-word/new-word/g' *.txt
```

http://www.bsd.org/


`/etc/fstab`
`http://smilejay.com/2011/03/fstab/`

atop  http://www.atoptool.nl/

![http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/linux_trouble_shot.jpg](http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/linux/linux_trouble_shot.jpg)


`lsof`
```
/proc/pid/maps
/proc/pid/fd/
```


**常用用命令:**

```
# uname -a # 查看内核/操作系统/CPU信息
# head -n 1 /etc/issue # 查看操作系统版本
# cat /proc/cpuinfo # 查看CPU信息
# hostname # 查看计算机名
# lspci -tv # 列出所有PCI设备
# lsusb -tv # 列出所有USB设备
# lsmod # 列出加载的内核模块
# env # 查看环境变量
# free -m # 查看内存使用量和交换区使用量
# df -h # 查看各分区使用情况
# du -sh < 目录名> # 查看指定目录的大小
# grep MemTotal /proc/meminfo # 查看内存总量
# grep MemFree /proc/meminfo # 查看空闲内存量
# uptime # 查看系统运行时间、用户数、负载
# cat /proc/loadavg # 查看系统负载
# mount | column -t # 查看挂接的分区状态
# fdisk -l # 查看所有分区
# swapon -s # 查看所有交换分区
# hdparm -i /dev/hda # 查看磁盘参数(仅适用于IDE设备)
# dmesg | grep IDE # 查看启动时IDE设备检测状况
# ifconfig # 查看所有网络接口的属性
# iptables -L # 查看防火墙设置
# route -n # 查看路由表
# netstat -lntp # 查看所有监听端口
# netstat -antp # 查看所有已经建立的连接
# netstat -s # 查看网络统计信息
# ps -ef # 查看所有进程
# top # 实时显示进程状态
# w # 查看活动用户
# id < 用户名> # 查看指定用户信息
# last # 查看用户登录日志
# cut -d: -f1 /etc/passwd # 查看系统所有用户
# cut -d: -f1 /etc/group # 查看系统所有组
# crontab -l # 查看所有用户的定时任务
```

<br><br>
<h3>参考资料</h3>
<code>[1].</code> <a href='http://www.hungry.com/~jamie/xexport.html'>http://www.hungry.com/~jamie/xexport.html</a><br>
<code>[2].</code> <a href='http://smilejay.com/2011/03/fstab/'>http://smilejay.com/2011/03/fstab/</a><br>
<code>[3].</code> <a href='http://www.cnblogs.com/liuhao/archive/2012/11/13/2768898.html'>http://www.cnblogs.com/liuhao/archive/2012/11/13/2768898.html</a><br>
<code>[4].</code> <a href='http://www.aqee.net/what-are-the-most-useful-swiss-army-knife-one-liners-on-unix/'>http://www.aqee.net/what-are-the-most-useful-swiss-army-knife-one-liners-on-unix/</a><br>
<code>[5].</code> <a href='http://linux.cn/portal-view-aid-211.html'>http://linux.cn/portal-view-aid-211.html</a><br>
<code>[6].</code> <a href='http://weibo.com/jiangbinglover'>http://weibo.com/jiangbinglover</a><br>
<code>[7].</code> <a href='http://www.cyberciti.biz/faq/unix-linux-replace-string-words-in-many-files/'>http://www.cyberciti.biz/faq/unix-linux-replace-string-words-in-many-files/</a><br>
<code>[8].</code> <a href='http://jsczxy2.iteye.com/blog/2112011'>http://jsczxy2.iteye.com/blog/2112011</a><br>