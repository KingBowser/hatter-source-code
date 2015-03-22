
```
SELECT /*+ index(t1 t1_abc) index(t2 t2_abc) */ COUNT(*)
FROM t1, t2
WHERE t1.col1 = t2.col1;
```



### 参考资料 ###
`[1].` http://psoug.org/reference/hints.html<br>