!!EXPLAIN
markdowndocs

`markdowndocs`使用的[!`Bootstrap`](http://getbootstrap.com/)作为UI，其中也使用了`prettify`和`scrollToTop`插件。`markdown`合并使用了[!`markdown4j`](https://code.google.com/p/markdown4j/)，HTML渲染和HTTPServer使用了`jsspserver`。

应用依赖的jar包如下：
* `commons-1.0.jar` - 工具类库
* `fastjson-1.1.24.jar` - 用于JSON解析
* `jsspserver-1.0.jar` - 用于jssp合并及HTTPServer
* `markdown4j-2.2.jar` - 用于合并markdown
* `thumbnailator-0.4.7.jar` - 用于生成缩略图

使用方法参看：
* [!](https://code.google.com/p/markdown4j/)
* [!](https://github.com/rjeschke/txtmark)
* [!](http://daringfireball.net/projects/markdown/)

改进功能：
* 连接`[]()`
    * `[](http://example.com)` 输出：
```
<a href="http://example.com">http://example.com</a>
```
    * `[!](http://example.com)` 输出：
```
<a href="http://example.com" target="_blank">http://example.com</a>
```
    * `[!Example](http://example.com)` 输出：
```
<a href="Example" target="_blank">http://example.com</a>
```


!!#ext# 自定义扩展

#### Wiki方式表格输出
```
%%% wikiTable [class=table___table-bordered style=width:600px;]

%%%
```


#### 代码美化输出
```
%%% prettify [ln=1 rl=1]

%%%
```
