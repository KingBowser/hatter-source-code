今天建了一个MySQL数据库，并创建了一个用户，但用户在登陆的时候提示没有权限 `Access denied for user 'samplename'@'localhost' (using password: YES)`。

但我已经做过grant操作了，命令如下：
```
mysql> show grants for samplename;
+-----------------------------------------------------------------------------------------------------------+
| Grants for samplename@%                                                                                   |
+-----------------------------------------------------------------------------------------------------------+
| GRANT USAGE ON *.* TO 'samplename'@'%' IDENTIFIED BY PASSWORD '*24C6553FA59884E1FA2C2AE57DD73AB2311DC649' |
| GRANT ALL PRIVILEGES ON `samplename_dev`.* TO 'samplename'@'%'                                            |
+-----------------------------------------------------------------------------------------------------------+
```

登陆提示如下：
```
# mysql -u samplename -h localhost -p 
Enter password: 
ERROR 1045 (28000): Access denied for user 'samplename'@'localhost' (using password: YES)
```

经过查找资料发现应该再做以下grant操作：
```
mysql> GRANT ALL PRIVILEGES ON `samplename_dev`.* TO 'samplename'@'localhost';
Query OK, 0 rows affected (0.00 sec)
```

但登陆时还是报错，查看user表：
```
mysql> SELECT user,host,password FROM mysql.user;
+------------+-----------------+-------------------------------------------+
| user       | host            | password                                  |
+------------+-----------------+-------------------------------------------+
| samplename | %               | *24C6553FA59884E1FA2C2AE57DD73AB2311DC649 |
| samplename | localhost       |                                           |
+------------+-----------------+-------------------------------------------+
```

发现 `samplename@'localhost'` 这个账号没有设置密码，通过set password设置密码：
```
mysql> SELECT user,host,password FROM mysql.user;
+------------+-----------------+-------------------------------------------+
| user       | host            | password                                  |
+------------+-----------------+-------------------------------------------+
| samplename | %               | *24C6553FA59884E1FA2C2AE57DD73AB2311DC649 |
| samplename | localhost       | *24C6553FA59884E1FA2C2AE57DD73AB2311DC649 |
+------------+-----------------+-------------------------------------------+
```

现次登陆OK：
```
# mysql -u samplename -h localhost -p
Enter password: 
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is 330
Server version: 5.1.61-log Source distribution

Copyright (c) 2000, 2011, Oracle and/or its affiliates. All rights reserved.

Oracle is a registered trademark of Oracle Corporation and/or its
affiliates. Other names may be trademarks of their respective
owners.

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> 
```

## 结论 ##
在MySQL中`@'localhost'`是一种特殊的逻辑，即`samplename@'%'` 和 `samplename@'localhost'` 是两个不同的用户，包括密钥和权限都是相互独立的。

### 参考资料 ###
`[1].` http://stackoverflow.com/questions/10299148/mysql-error-1045-28000-access-denied-for-user-billlocalhost-using-passw <br>
<code>[2].</code> <a href='http://dev.mysql.com/doc/refman//5.5/en/set-password.html'>http://dev.mysql.com/doc/refman//5.5/en/set-password.html</a><br>
<code>[3].</code> <a href='http://dev.mysql.com/doc/refman/5.0/en/drop-user.html'>http://dev.mysql.com/doc/refman/5.0/en/drop-user.html</a><br>