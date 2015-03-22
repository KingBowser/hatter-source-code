# `show xxx;` commands #
| _`show variables`_ | 查看MySQL变量 |
|:-------------------|:------------------|
| _`show status`_ |  |
| _`show processlist`_ | 查询登陆到MySQL的用户及执行中的SQL |
| _`show master status`_ | 查询binlog位点信息 |
| _`show slave status`_ |  |
| _`show innodb status`_ |  |
| _`show table status`_ | 查询表的统计信息 |
| _`show databases`_ | 查询有哪些库 |
| _`show tables`_ | 查询当前库有哪些表 |
| _`show binary logs`_ | 查询当前有哪些binlog |
| _`show binlog events [limit N]`_ | 查看binlog日志【注意不加limit可能会卡死】 |
| _`show character set`_ | 查看当前支持的字符集 |
| _`show collation`_ | 查看支持的排序方式 |
| _`show columns from _table name_`_ | 同desc table name |
| _``_ |  |
| _``_ |  |
| _``_ |  |


```
SHOW AUTHORS
SHOW {BINARY | MASTER} LOGS
SHOW BINLOG EVENTS [IN 'log_name'] [FROM pos] [LIMIT [offset,] row_count]
SHOW CHARACTER SET [like_or_where]
SHOW COLLATION [like_or_where]
SHOW [FULL] COLUMNS FROM tbl_name [FROM db_name] [like_or_where]
SHOW CONTRIBUTORS
SHOW CREATE DATABASE db_name
SHOW CREATE EVENT event_name
SHOW CREATE FUNCTION func_name
SHOW CREATE PROCEDURE proc_name
SHOW CREATE TABLE tbl_name
SHOW CREATE TRIGGER trigger_name
SHOW CREATE VIEW view_name
SHOW DATABASES [like_or_where]
SHOW ENGINE engine_name {STATUS | MUTEX}
SHOW [STORAGE] ENGINES
SHOW ERRORS [LIMIT [offset,] row_count]
SHOW EVENTS
SHOW FUNCTION CODE func_name
SHOW FUNCTION STATUS [like_or_where]
SHOW GRANTS FOR user
SHOW INDEX FROM tbl_name [FROM db_name]
SHOW MASTER STATUS
SHOW OPEN TABLES [FROM db_name] [like_or_where]
SHOW PLUGINS
SHOW PROCEDURE CODE proc_name
SHOW PROCEDURE STATUS [like_or_where]
SHOW PRIVILEGES
SHOW [FULL] PROCESSLIST
SHOW PROFILE [types] [FOR QUERY n] [OFFSET n] [LIMIT n]
SHOW PROFILES
SHOW SLAVE HOSTS
SHOW SLAVE STATUS
SHOW [GLOBAL | SESSION] STATUS [like_or_where]
SHOW TABLE STATUS [FROM db_name] [like_or_where]
SHOW [FULL] TABLES [FROM db_name] [like_or_where]
SHOW TRIGGERS [FROM db_name] [like_or_where]
SHOW [GLOBAL | SESSION] VARIABLES [like_or_where]
SHOW WARNINGS [LIMIT [offset,] row_count]

like_or_where:
    LIKE 'pattern'
  | WHERE expr
```


### 参考资料 ###
`[1].` http://dev.mysql.com/doc/refman/5.5/en/show.html<br>