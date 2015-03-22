MySQL InnoDB存储引擎，实现的是基于多版本的并发控制协议——MVCC (Multi-Version Concurrency Control) (注：与MVCC相对的，是基于锁的并发控制，Lock-Based Concurrency Control)。MVCC最大的好处：读不加锁，读写不冲突。几乎所有的RDBMS，都支持了MVCC。

  * **快照读(snapshot read)**：简单的select操作，属于快照读，不加锁。(也有例外)
    * `select * from table where ?;`
  * **当前读(current read)**：特殊的读操作，插入/更新/删除操作，属于当前读，需要加锁。
    * `select * from table where ? lock in share mode;`
    * `select * from table where ? for update;`
    * `insert into table values (…);`
    * `update table set ? where ?;`
    * `delete from table where ?;`
> 除了第一条语句，对读取记录加S锁 (共享锁)外，其他的操作，都加的是X锁 (排它锁)。

MySQL/InnoDB定义的4种隔离级别：
  * **Read Uncommited**
> 可以读取未提交记录。此隔离级别，不会使用，忽略。
  * **Read Committed (RC)**
> 快照读忽略，本文不考虑。
> 针对当前读，RC隔离级别保证对读取到的记录加锁 (记录锁)，存在幻读现象。
  * **Repeatable Read (RR)**
> 快照读忽略，本文不考虑。
> 针对当前读，RR隔离级别保证对读取到的记录加锁 (记录锁)，同时保证对读取的范围加锁，新的满足查询条件的记录不能够插入 (间隙锁)，不存在幻读现象。
  * **Serializable**
> 从MVCC并发控制退化为基于锁的并发控制。不区别快照读与当前读，所有的读操作均为当前读，读加读锁 (S锁)，写加写锁 (X锁)。
> Serializable隔离级别下，读写冲突，因此并发度急剧下降，在MySQL/InnoDB下不建议使用。

### 参考资料 ###
`[1].` http://dev.mysql.com/doc/refman/5.0/en/innodb-lock-modes.html<br>
<code>[2].</code> <a href='http://dev.mysql.com/doc/refman/5.0/en/innodb-record-level-locks.html'>http://dev.mysql.com/doc/refman/5.0/en/innodb-record-level-locks.html</a><br>
<code>[3].</code> <a href='http://dev.mysql.com/doc/refman/5.0/en/innodb-next-key-locking.html'>http://dev.mysql.com/doc/refman/5.0/en/innodb-next-key-locking.html</a><br>
<code>[4].</code> <a href='http://hedengcheng.com/?p=771'>http://hedengcheng.com/?p=771</a><br>