# Explain #
  * `explain <QUERY>;`


# Profile #
  * `SELECT @@profiling;`<br>查看是否已经打开Profile<br>
<ul><li><code>SET profiling = 1;</code><br>打开Profile<br>
</li><li><code>&lt;execute sql query&gt;;</code><br>执行SQL语句<br>
</li><li><code>show profiles;</code><br>查看做过Profile的SQL<br>
</li><li><code>show profile for query &lt;N&gt;;</code><br>打印对应SQL的Profile数据</li></ul>

<h3>参考资料</h3>
<code>[1].</code> <a href='http://dev.mysql.com/doc/refman/5.6/en/explain-output.html'>http://dev.mysql.com/doc/refman/5.6/en/explain-output.html</a><br>
<code>[2].</code> <a href='http://hi.baidu.com/gguoyu/item/4a588a0f41b9cb69d55a115f'>http://hi.baidu.com/gguoyu/item/4a588a0f41b9cb69d55a115f</a><br>