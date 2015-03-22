# Collation: #
```
charset_[chinese|japanese|general|...]_[ci|cs|...]
```

#### `ci|cs|...` ####
  * ci 不区分大小写
  * cs 区分大小写

## 如何查看： ##
  * 查看支持哪些collation:
> `show collation [like '%utf%'];`
  * 查看表的默认collation:
> `show table status [like '%table_name%'];`
  * 查看字段的collation:
```
SELECT TABLE_CATALOG, TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, COLLATION_NAME
FROM INFORMATION_SCHEMA.COLUMNS [where table_name ='table_name'];
```