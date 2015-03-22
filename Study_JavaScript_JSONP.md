在ECMAScript的规范里面有这样一段记载。除了`0x0A/0x0D`以外`U+2028/2029`也可以作为换行符来使用。

**Line Terminator Characters** from [7.3 Line Terminators](http://www.ecma-international.org/ecma-262/5.1/#sec-7.3)

| **Code Unit Value** | **Name** | **Formal Name** |
|:--------------------|:---------|:----------------|
| `\u000A` | Line Feed | `<LF>` |
| `\u000D` | Carriage Return | `<CR>` |
| `\u2028` | Line separator | `<LS>` |
| `\u2029` | Paragraph separator | `<PS>` |


在IE和chrome中对于【#】后面出现的`U+2028/2029`不会进行encode而直接包含在URL当中。

```
<script>
//[U+2028]alert(1)
</script>
```


## 行结束符将 C# 源文件的字符划分为行 ##

new-line:（新行：）

  * 回车符 (U+000D)
  * 换行符 (U+000A)
  * 回车符 (U+000D) 后跟换行符 (U+000A)
  * 行分隔符 (U+2028)
  * 段落分隔符 (U+2029)

为了与添加文件尾标记的源代码编辑工具兼容，并能够以正确结束的行序列的形式查看源文件，下列转换按顺序应用到 C# 程序中的每个源文件：
如果源文件的最后一个字符为 Control-Z 字符 (U+001A)，则删除此字符。
如果源文件非空并且源文件的最后一个字符不是回车符 (U+000D)、换行符 (U+000A)、行分隔符 (U+2028) 或段落分隔符 (U+2029)，则将在源文件的结尾添加一个回车符 (U+000D)。




### 参考资料 ###
`[1].` https://github.com/rack/rack-contrib/blob/master/lib/rack/contrib/jsonp.rb#L23<br>
<code>[2].</code> <a href='http://masatokinugawa.l0.cm/2013/09/u2028u2029.domxss.html'>http://masatokinugawa.l0.cm/2013/09/u2028u2029.domxss.html</a>  <a href='http://www.hack80.com/thread-21505-1-1.html'>zh-CN</a><br>
<code>[3].</code> <a href='http://msdn.microsoft.com/zh-cn/library/aa664664(v=vs.71).aspx'>http://msdn.microsoft.com/zh-cn/library/aa664664(v=vs.71).aspx</a><br>
<code>[4].</code> <a href='http://php-tips.com/2009/11/23/%E3%83%A1%E3%83%A2-u2029%E3%81%8C%E5%90%AB%E3%81%BE%E3%82%8C%E3%82%8Bjson%E3%83%87%E3%83%BC%E3%82%BF%E3%82%92eval%E9%96%A2%E6%95%B0%E3%82%92%E4%BD%BF%E3%81%A3%E3%81%A6%E3%83%87%E3%82%B3%E3%83%BC/'>http://php-tips.com/2009/11/23/メモ-u2029が含まれるjsonデータをeval関数を使ってデコー/</a><br>
<code>[5].</code> <a href='http://subtech.g.hatena.ne.jp/mala/20101122/1290436563'>http://subtech.g.hatena.ne.jp/mala/20101122/1290436563</a><br>
<code>[6].</code> <a href='http://qiita.com/kjunichi/items/4c9d0cb408fbc846799f'>http://qiita.com/kjunichi/items/4c9d0cb408fbc846799f</a><br>
<code>[7].</code> <a href='http://www.unicode.org/reports/tr13/tr13-9.html'>http://www.unicode.org/reports/tr13/tr13-9.html</a><br>
<code>[8].</code> <a href='http://www.ecma-international.org/ecma-262/5.1/'>http://www.ecma-international.org/ecma-262/5.1/</a><br>
<code>[9].</code> <a href='http://unicode.org/reports/tr14/'>http://unicode.org/reports/tr14/</a><br>
<code>[10].</code> <a href='http://en.wikipedia.org/wiki/Newline'>http://en.wikipedia.org/wiki/Newline</a><br>