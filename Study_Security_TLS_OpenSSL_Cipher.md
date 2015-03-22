Sample:
```
openssl ciphers -[v|V] 'HIGH:!aNULL:!MD5'
```

| **Prefix** | **Description** |
|:-----------|:----------------|
| `e` | 加密 |
| `a` | 认证 |
| `k` | 密钥交换 |

| **Operator** | **Description** |
|:-------------|:----------------|
| `!` | 永久排除，被排除的加密算法不会再被加回来 |
| `-` | 排除，但被排除的加密算法可被加回来 |
| `+` | 设置排序到最后 |


| **Chiper** | **Description** |
|:-----------|:----------------|
| **DEFAULT** | `ALL:!aNULL:!eNULL` |
| **COMPLEMENTOFDEFAULT** | `aNULL` ?(说明实在看不懂，但从结果看和`aNULL`是一样的) |
| **COMPLEMENTOFALL** | `eNULL` |
| **ALL** | 除了`eNULL`的所有加密算法 |
| **HIGH** | - |
| **MEDIUM** | - |
| **LOW** | - |
| **EXPORT** | 可出口，应该是美国对于加密算法出口的限制，所以可出口的的加密算法都非常弱 |
| **eNULL** | 即`NULL`，不加密 |
| **aNULL** | 不认证 |



### 参考资料 ###
`[1].` https://www.openssl.org/docs/apps/ciphers.html<br>