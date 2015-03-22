<table>
<tr><td>
<img src='http://hatter-source-code.googlecode.com/svn/trunk/attachments/wiki/golang/Golang.png' />
</td><td>  </td><td>
<font size='6'>The Go Programming Language</font>
</td></tr>
</table>

```
package main

import "fmt"

func main() {
  fmt.Println("你好，世界！")
}
```

| **官方网站** | http://golang.org 当无法访问该地址是请访问：http://go.hatter.me, http://playgo.hatter.me |
|:-----------------|:------------------------------------------------------------------------------------------------------|
| **官方博客** | http://blog.golang.org/ 通过Google Reader订阅该地址：http://blog.golang.org/feeds/posts/default |
| 授权 | [BSD-Style License](http://golang.org/LICENSE) |

| **支持架构** | `i386, amd64, ARM` |
|:-----------------|:-------------------|
| **支持操作系统** | `Linux, Mac OS X, FreeBSD, OpenBSD, Plan 9` |


## 开发人员 ##
  * Go语言的主要作者：
    * 肯•汤普逊（Ken Thompson，http://en.wikipedia.org/wiki/Ken_Thompson）：设计了B语言和C语言，创建了Unix和Plan 9操作系统，1983年图灵奖得主，Go的共同作者。
    * 罗布•派克（Rob Pike，http://en.wikipedia.org/wiki/Rob_Pike）：Unix小组的成员，参与Plan 9和Inferno操作系统，参与 Limbo和Go语言的研发。《UNIX编程环境》作者之一。
    * 罗伯特•格里泽默（Robert Griesemer）：曾协助制作Java的HotSpot编译器和Chrome浏览器的JavaScript引擎V8。
    * 拉斯•考克斯（Russ Cox，http://swtch.com/~rsc/）：参与Plan 9操作系统的开发，Google Code Search项目负责人。
    * 伊安•泰勒（Ian Lance Taylor）：GCC社区的活跃人物。gold连接器和GCC过程间优化LTO的主要设计者，Zembu公司的创始人。
    * 布拉德•菲茨帕特里克（Brad Fitzpatrick，http://en.wikipedia.org/wiki/Brad_Fitzpatrick）：LiveJournal 的创始人，著名开源项目memcached的作者。
  * 其它贡献者
    * http://golang.org/CONTRIBUTORS

## 荣誉 ##
| 2010 | InfoWorld BOSSIE |
|:-----|:-----------------|
| 2009 | TIOBE "Language of the year" |

## 安装FAQ ##
  * CentOS 6 安装时提示：`/lib/ld-linux.so.2: bad ELF interpreter: No such file or directory`
> 尝试安装：`yum install glibc.i686`

## IDE ##
| `LiteIDE` | http://code.google.com/p/golangide/ | Go语言写的跨平台Go语言IDE |
|:----------|:------------------------------------|:-----------------------------------|
| `Go IDE` | http://go-ide.com/ | Google Go Language IDE (based on Intellij IDEA platform) |

## Library ##
| `goquery` | https://github.com/PuerkitoBio/goquery | a little like that j-thing, only in Go |
|:----------|:---------------------------------------|:---------------------------------------|

## SQLDrivers ##
http://code.google.com/p/go-wiki/wiki/SQLDrivers


### 参考资料 ###
`[1].` http://golang.org<br>
<code>[2].</code> <a href='http://code.google.com/p/golang-china/'>http://code.google.com/p/golang-china/</a><br>
<code>[3].</code> <a href='http://code.google.com/p/golangide/'>http://code.google.com/p/golangide/</a><br>
<code>[4].</code> <a href='https://github.com/wonderfo/wonderfogo/wiki'>https://github.com/wonderfo/wonderfogo/wiki</a><br>
<code>[5].</code> <a href='http://en.wikipedia.org/wiki/Go_(programming_language)'>http://en.wikipedia.org/wiki/Go_(programming_language)</a><br>
<code>[6].</code> <a href='http://en.wikipedia.org/wiki/Limbo_(programming_language)'>http://en.wikipedia.org/wiki/Limbo_(programming_language)</a><br>
<code>[7].</code> <a href='http://en.wikipedia.org/wiki/Plan_9_from_Bell_Labs'>http://en.wikipedia.org/wiki/Plan_9_from_Bell_Labs</a><br>
<code>[8].</code> <a href='http://www.cnblogs.com/turingbooks/archive/2012/08/02/2620199.html'>http://www.cnblogs.com/turingbooks/archive/2012/08/02/2620199.html</a><br>
<code>[9].</code> <a href='http://zh.wikipedia.org/wiki/%E9%B8%AD%E5%AD%90%E7%B1%BB%E5%9E%8B'>http://zh.wikipedia.org/wiki/%E9%B8%AD%E5%AD%90%E7%B1%BB%E5%9E%8B</a><br>