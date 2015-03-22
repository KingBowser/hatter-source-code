# 热备 #
XtraBackup

# 冷备 #
拷贝数据库相关文件，包括：frm文件、共享表空间文件、独立表空间文件（**.ibd）、重做日志文件及配置文件my.cnf**

mysqldump
```
$ mysqldump [arguments] > file_name

$ mysqldump --all-databases > dump.sql
$ mysqldump --databases db1 db2 db3 > dump.sql
```

# 温备 #