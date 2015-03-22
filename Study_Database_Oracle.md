
```
echo $ORACLE_HOME
echo $ORACLE_SID
ps -ef | grep smon

/proc/<PID>/environ

exp \"sys/oracle as sysdba\" file=/mnt/exp/db.dmp log=/mnt/exp/db.log full=y rows=n compress=y direct=n

imp \"sys/oracle as sysdba\" file=db.dmp ignore=y full=y


select tablespace_name,file_name from SYS.DBA_DATA_FILES;


alter tablespace TABLE_SPACE_NAME add datafile 'xxx.dbf' size 5000M reuse AUTOEXTEND ON NEXT 200M MAXSIZE UNLIMITED;
```

计算表空间空闲空间：
```
SELECT UPPER(D.TABLESPACE_NAME) "TableSpaceName",
       D.TOT_GROOTTE_MB "Total(M)",
       D.TOT_GROOTTE_MB - nvl(F.TOTAL_BYTES, 0) "Used(M)",
       TO_CHAR(ROUND((D.TOT_GROOTTE_MB - nvl(F.TOTAL_BYTES, 0)) / D.TOT_GROOTTE_MB * 100, 2), '990.99')||'%' "Used(%)",
       nvl(F.TOTAL_BYTES, 0) "Free(M)", F.MAX_BYTES "MaxBlock(M)"
  FROM (SELECT TABLESPACE_NAME,
               ROUND(SUM(BYTES) / (1024 * 1024), 2) TOTAL_BYTES,
               ROUND(MAX(BYTES) / (1024 * 1024), 2) MAX_BYTES
          FROM SYS.DBA_FREE_SPACE
         GROUP BY TABLESPACE_NAME) F,
       (SELECT DD.TABLESPACE_NAME,
               ROUND(SUM(DD.BYTES) / (1024 * 1024), 2) TOT_GROOTTE_MB
          FROM SYS.DBA_DATA_FILES DD
         GROUP BY DD.TABLESPACE_NAME) D
 WHERE D.TABLESPACE_NAME = F.TABLESPACE_NAME(+)
 ORDER BY F.TABLESPACE_NAME;
```


## 权限 ##
授予权限：
```
GRANT privilege [, privilege...] TO user [, user| role, PUBLIC...]
       [WITH ADMIN OPTION];  // 是否用户可再授权限给别的用户
```

| `CREATE SESSION` | 创建会话 |
|:-----------------|:-------------|
| `CREATE SEQUENCE` | 创建序列 |
| `CREATE SYNONYM` | 创建同名对象 |
| `CREATE TABLE` | 在用户模式中创建表 |
| `CREATE ANY TABLE` | 在任何模式中创建表 |
| `DROP TABLE` | 在用户模式中删除表 |
| `DROP ANY TABLE` | 在任何模式中删除表 |
| `CREATE PROCEDURE` | 创建存储过程 |
| `EXECUTE ANY PROCEDURE` | 执行任何模式的存储过程 |
| `CREATE USER` | 创建用户 |
| `DROP USER` | 删除用户 |
| `CREATE VIEW` | 创建视图 |

查看权限：
```
SELECT grantee,privilege,admin_option FROM dba_sys_privs
               WHERE grantee IN ('_DB_');
```

  * 显示所有用户：`select * from all_users;`
  * 显示所有Table：`select * from all_tables;`
  * 查看tablespace空间：`select tablespace_name,round(sum(bytes)/1024/1024,2) "M" from dba_free_space group by tablespace_name;`

账号被锁：
  * 查看被锁用户及时间：` select username,lock_date from dba_users;`
  * 解锁：`ALTER USER username [IDENTIFIED BY password] ACCOUNT UNLOCK;`
  * 查看设置：`select * from dba_profiles where RESOURCE_NAME = 'FAILED_LOGIN_ATTEMPTS';`
  * 修改为30次：`alter profile default limit FAILED_LOGIN_ATTEMPTS 30;`
  * 修改为没有限制：`alter profile default limit FAILED_LOGIN_ATTEMPTS unlimited;`