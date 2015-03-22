[B+ Tree Indexes and InnoDB](http://hatter-source-code.googlecode.com/svn/trunk/attachments/pdfs/mysql/B+Tree_Indexes_and_InnoDB.pdf)

## 安装 ##
```
// 安装
rpm -ivh MySQL_server_X.X.XX.linux2.6.x86_64.rpm
// 启动
service mysql start
// 停止
service mysql stop

service mysql {start|stop|restart|reload|force-reload|status}

/var/log/mysqld.log

mysql -u root
mysql -h <IP> -u <USERNAME> -p

查看版本: select version();
```

## 存储引擎 ##
| **引擎** | **说明** |
|:-----------|:-----------|
| MyISAM | MyISAM was the default storage engine for the MySQL relational database management system versions prior to 5.5 |
| InnoDB | InnoDB is the default storage engine for MySQL as of MySQL 5.5 |
| TokuDB | TokuDB is a storage engine for MySQL and MariaDB that is specifically designed for high performance on write-intensive workloads |

## 维护 ##
  * 查看有哪些用户 `select * from mysql.user`

## SQL ##
  * DATE\_ADD `DATE_ADD(date,INTERVAL expr type) , DATE_SUB(date,INTERVAL expr type) , date + expr type` <br> <code>expr type</code> sample: <code>INTERVAL 1 SECOND, INTERVAL 1 DAY etc.</code>
<ul><li>GROUP CONCAT <code>select type, group_concat(id) from table group by type;</code></li></ul>

<pre><code>create database databasename;<br>
<br>
drop table if exists tablename;<br>
</code></pre>

<h2>锁</h2>
<ul><li>当update or delete，where条件中为非索引条件时，锁全表。这是为什么在项目上线时大量死锁的原因。<br>
</li><li>当update or delete，where条件中为非主键索引时，根据非主键索引找到对应的主键索引，并锁主建对应的记录。虽为行锁，但锁了多行。并且，mysql的行锁还有一个很隐蔽的问题，即使用主键索引，在一定条件下，可能会锁相邻记录。也就是说，锁的范围超出真正需要update的行。对这样的情形，使用where id=888的样式，逐行更新记录，则是非常安全的行为。<br>
</li><li>MySQL drop table可能会导致全库hang住，所以删除大表，最好是通过硬链接的方式，为物理文件建立一个硬链接，然后从数据库中删除后，最后再删除物理文件。<br>
</li><li>并发插入数据导致死锁 <sup>[3]</sup></li></ul>

<h2>事务隔离级别 <sup>[4]</sup></h2>

<table><thead><th> <b>隔离级别</b> </th><th> <b>是否脏读</b> </th><th> <b>是否不可重复读</b> </th><th> <b>是否幻读</b> </th></thead><tbody>
<tr><td> 未提交读 (Read uncommit ) </td><td> Y </td><td> Y </td><td> Y </td></tr>
<tr><td> 提交读 (Read Committed) </td><td> N </td><td> Y </td><td> Y </td></tr>
<tr><td> 可重复读 (Repeatable Read) </td><td> N </td><td> N </td><td> Y </td></tr>
<tr><td> 序列化读 (Serializable Read) </td><td> N </td><td> N </td><td> N </td></tr></tbody></table>

<ul><li>脏读：还未提交的内容可以被读取到<br>
</li><li>不可重复读：就是假如A查上来一个数据叫张三，另一个人把张三改成了李四，A刷新，数据变成了李四，重复读取数据不对了，张三变成李四了，这就是不可重复读<br>
</li><li>幻读：比如A查询age<20的记录，查询到5条，再次刷新，记录变成了10条，这是因为期间别人又录入了5条</li></ul>

MySQL 默认是可重复读。<br>
<pre><code>// 查看事务隔离级别<br>
select @@tx_isolation;<br>
<br>
// 设置事务隔离级别<br>
set session transaction isolation level READ UNCOMMITTED;<br>
</code></pre>

<h2>性能</h2>
MySQL INSERT性能优化，将多条INSERT语句合并成一条INSERT语句，在事务中处理也会有性能提升。<br>
<pre><code>INSERT INTO `insert_table` (`datetime`, `uid`, `content`, `type`) VALUES ('0', 'userid_0', 'content_0', 0);<br>
INSERT INTO `insert_table` (`datetime`, `uid`, `content`, `type`) VALUES ('1', 'userid_1', 'content_1', 1);<br>
</code></pre>
合并为：<br>
<pre><code>INSERT INTO `insert_table` (`datetime`, `uid`, `content`, `type`) VALUES <br>
('0', 'userid_0', 'content_0', 0), <br>
('1', 'userid_1', 'content_1', 1);<br>
</code></pre>
相关数据：<br>
<table><thead><th> <b>记录数</b> </th><th> <b>单条数据插入</b> </th><th> <b>合并数据+事务插入</b> </th></thead><tbody>
<tr><td> 1万 </td><td> 0m15.977s </td><td> 0m0.309s </td></tr>
<tr><td> 10万 </td><td> 1m52.204s </td><td> 0m2.271s </td></tr>
<tr><td> 100万 </td><td> 18m31.317s </td><td> 0m23.332s </td></tr></tbody></table>

<h2>MySQL特殊点</h2>
MySQL在执行UPDATE时，如果更新前和更新后数据一样时不会产生binlog日志，Oracle会产生日志<br>
<br>
<h2>用户/权限</h2>
<pre><code>CREATE USER 'username'@'%' IDENTIFIED BY 'password';<br>
<br>
SHOW GRANTS FOR 用户名@域名或IP<br>
  show grants for username;<br>
<br>
GRANT 权限列表 ON 数据库名.表名 TO 用户名@来源地址 [IDENTIFIED BY '密码']<br>
  grant select,insert,update,delete on schema.* to username@'%' identified by 'password';<br>
<br>
REVOKE 权限列表 ON 数据库名.表名 FROM 用户名@域名或IP<br>
  revoke ALL on schema.* from username@'%';<br>
<br>
grant ALL PRIVILEGES on *.* to username@'%' identified by 'password' [WITH GRANT OPTION].<br>
<br>
FLUSH PRIVILEGES;<br>
<br>
show [full] processlist;<br>
<br>
show variables; //  查看MySQL参数<br>
</code></pre>

<h2>函数</h2>
<pre><code>STR_TO_DATE('2013-04-10 00:00:00', '%Y-%m-%d %H:%i:%s')<br>
<br>
String 转数字：<br>
cast(table_field as signed)<br>
</code></pre>

<h2>自增长ID</h2>
MySQL中可以通过环境变量设置自增长ID的生成步长及起始值：<br>
<pre><code>SHOW VARIABLES LIKE 'auto_inc%';<br>
auto_increment_increment<br>
auto_increment_offset<br>
</code></pre>

<h2>独立表空间</h2>
<pre><code>innodb_file_per_table=1<br>
</code></pre>


<pre><code>The TIMESTAMP data type has a range of '1970-01-01 00:00:01' UTC to '2038-01-09 03:14:07' UTC.<br>
</code></pre>


Google MySQL:<br>
<a href='https://code.google.com/p/google-mysql/'>https://code.google.com/p/google-mysql/</a>

<a href='http://www.mysqlperformanceblog.com/'>http://www.mysqlperformanceblog.com/</a>


<h3>参考资料</h3>
<code>[1].</code> <a href='http://firedragonpzy.iteye.com/blog/1388562'>http://firedragonpzy.iteye.com/blog/1388562</a><br>
<code>[2].</code> <a href='http://blog.jobbole.com/29432/'>http://blog.jobbole.com/29432/</a><br>
<code>[3].</code> <a href='http://thushw.blogspot.com/2010/11/mysql-deadlocks-with-concurrent-inserts.html'>http://thushw.blogspot.com/2010/11/mysql-deadlocks-with-concurrent-inserts.html</a><br>
<code>[4].</code> <a href='http://www.zhurouyoudu.com/index.php/archives/735/'>http://www.zhurouyoudu.com/index.php/archives/735/</a><br>
<code>[5].</code> <a href='http://dev.mysql.com/doc/refman/5.5/en/innodb-parameters.html'>http://dev.mysql.com/doc/refman/5.5/en/innodb-parameters.html</a><br>