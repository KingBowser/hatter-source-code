Linux控制台实现了VT102和ECMA-48/ISO 6429/ANSI X3.64终端控制的子集。

在Shell中试用该功能：
```
echo -e '\033[1;4;101mabc\033[0m'
```

|   `-`   | OCT | HEX | DEC |
|:--------|:----|:----|:----|
| `ESC` | `33` | `1B` | `27` |

当前屏幕清屏并输出的Java程序示例：[ScreenPrinter.java](http://hatter-source-code.googlecode.com/svn/trunk/commons/main/java/me/hatter/tools/commons/screen/impl/ScreenPrinter.java)

### 参数资料 ###
`[1].` http://www.cnblogs.com/mugua/archive/2009/11/25/1610118.html<br>
<code>[2].</code> <a href='http://www.kernel.org/doc/man-pages/online/pages/man4/console_codes.4.html'>http://www.kernel.org/doc/man-pages/online/pages/man4/console_codes.4.html</a><br>