## C/C++/Java/C#的基础类型模型的定义<sup>[2]</sup> ##
| **DATETYPE** | **LP32** | **ILP32** | **LP64** | **ILP64** | **LLP64** | **JAVA** | **C#** |
|:-------------|:---------|:----------|:---------|:----------|:----------|:---------|:-------|
| `CHAR`  | 8 | 8 | 8 | 8 | 8 | 16 | 16 |
| `BYTE` | _N/A_ | _N/A_ | _N/A_ | _N/A_ | _N/A_ | 8 | 8 |
| `SHORT` | 16 | 16 | 16 | 16 | 16 | 16 | 16 |
| `_INT32` | _N/A_ | 32 | _N/A_ | _N/A_ | _N/A_ | _N/A_ | _N/A_ |
| `INT` | 16 | 32 | 32 | 64 | 32 | 32 | 32 |
| `LONG` | 32 | 32 | 64 | 64 | 32 | 64 | 64 |
| `LONG LONG` | 64 | 64 | 64 | 64 | 64 | _N/A_ | _N/A_ |
| `POINTER` | 32 | 32 | 64 | 64 | 64 | _N/A_ | _N/A_ |
| `FLOAT` | 32 | 32 | 32 | 32 | 32 | 32 | 32 |
| `DOUBLE` | 64 | 64 | 64 | 64 | 64 | 64 | 64 |
| `BOOL` | _T/F_ | _T/F_ | _T/F_ | _T/F_ | _T/F_ | _T/F_ | _T/F_ |

上表中，LP64、ILP64、LLP64是64位平台上字长的数据模型，ILP32和LP32是32位平台上字长的数据模型。
  * LP64指的是LONG/POINTER字长为64位；
  * ILP64指的是INT/LONG/POINT字长为64位；
  * LLP64指的是LONGLONG/POINTER字长为64位；
  * ILP32指的是INT/LONG/POINTER字长为32位；
  * LP32指的是LONG/POINT字长是32位的，INT字长为16位。<br>
<sup>注1</sup>：32位Windows采用的是ILP32数据模型，64位Windows采用LLP64数据模型。<br>
<sup>注2</sup>：32位的Linux/Unix使用ILP32数据模型，64位Linux/Unix使用LP64数据模型。<br>
<sup>注3</sup>：为了增加代码的移植性，打印无符号整形数，不管申明时是如何定义的，统一使用 %lu。<br>
<sup>注4</sup>：为了保证平台的通用性，代码中尽量不要使用long数据库型。<br>
<sup>注5</sup>：使用INT时也可以使用intptr_t来保证平台的通用性，它在不同的平台上编译时长度不同，但都是标准的平台长度，比如：64位机器上长度为8字节，32位机器上长度为4字节。<br>
<sup>注6</sup>：编写代码时要尽量使用sizeof来计算数据类型的大小。<br>
<sup>注7</sup>：ssize_t和size_t分别是signsize_t和unsigned signed size of computer word size。它们也是表示计算机的字长，在32位机器上是int型，在64位机器上long型，从某种意义上来说它们等同于intptr_t和 uintptr_t。</li></ul>

<h3>参考资料</h3>
<code>[1].</code> <a href='http://www.yankay.com/go-clear-concurreny/'>http://www.yankay.com/go-clear-concurreny/</a><br>
<code>[2].</code> <a href='http://www.blogjava.net/jxlazzw/archive/2012/11/30/392280.html'>http://www.blogjava.net/jxlazzw/archive/2012/11/30/392280.html</a><br>
<code>[3].</code> <a href='http://blog.sae.sina.com.cn/archives/2054'>http://blog.sae.sina.com.cn/archives/2054</a><br>