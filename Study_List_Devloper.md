# _Moved to: http://p.rogram.me/java.study.list/_ #

# _This page will not update._ #

# `Java`工程师的学习列表 #

## 一、`Java`基础知识 ##
  1. 看一下以下类的源代码
    * `java.lang.String`
    * `java.lang.Enum`
    * `java.math.BigDecimal`
    * `java.lang.ThreadLocal`
    * `java.lang.ClassLoader` & `java.net.URLClassLoader`
    * `java.util.ArrayList & java.util.LinkedList`
    * `java.util.HashMap & java.util.LinkedHashMap & java.util.TreeMap`
    * `java.util.HashSet & java.util.LinkedHashSet & java.util.TreeSet`
  1. 浏览一下以下类的接口及说明
    * `java.io.*`
    * `java.lang.*`
    * `java.lang.management.*`
    * `java.lang.ref.*`
    * `java.lang.reflect.*`
    * `java.util.*`
    * `java.net.*`
  1. Java实现对Array/List排序
    * `java.uti.Arrays.sort()`
    * `java.util.Collections.sort()`
  1. Java实现对List去重
  1. Java实现对List去重，并且需要保留数据原始的出现顺序
  1. Java实现通过正则表达式提取一段文本中的电子邮件，并将 `@` 替换为 `#` 输出
  1. 学习使用常用的Java工具库
    * `commons.lang, commons.*, ...`
    * `guava-libraries`
    * `netty`
  1. 什么是 API & SPI ？
    * http://en.wikipedia.org/wiki/Application_programming_interface
    * http://en.wikipedia.org/wiki/Service_provider_interface
  1. 学习使用Java工具
    * `jps, jstack, jmap, jconsole, jinfo, jhat, javap, ...`
    * http://kenai.com/projects/btrace
    * http://www.crashub.org/
    * https://github.com/taobao/TProfiler
    * https://github.com/CSUG/HouseMD
    * http://wiki.cyclopsgroup.org/jmxterm
    * https://github.com/jlusdy/TBJMap
  1. 学习Java诊断工具
    * http://www.eclipse.org/mat/
    * http://visualvm.java.net/oqlhelp.html
  1. 使用工具尝试解决以下问题，并写下总结 【 _**没有问题也要创造问题、解决问题**_ 】
    * 当一个Java程序响应很慢时如何查找问题？
    * 当一个Java程序频繁 `FullGC` 时如何解决问题？
    * 当一个Java应用发生 `OutOfMemoryError` 时该如何解决？

#### 相关资料 ####
`[1].` JDK src.zip 源代码<br>
<code>[2].</code> <a href='http://commons.apache.org/'>http://commons.apache.org/</a><br>
<code>[3].</code> <a href='https://code.google.com/p/guava-libraries/'>https://code.google.com/p/guava-libraries/</a><br>
<code>[4].</code> <a href='http://netty.io/'>http://netty.io/</a><br>
<code>[5].</code> <a href='http://stackoverflow.com/questions/2954372/difference-between-spi-and-api'>http://stackoverflow.com/questions/2954372/difference-between-spi-and-api</a><br>
<code>[6].</code> <a href='http://stackoverflow.com/questions/11404230/how-to-implement-the-api-spi-pattern-in-java'>http://stackoverflow.com/questions/11404230/how-to-implement-the-api-spi-pattern-in-java</a><br>

<h2>二、<code>Java</code>并发编程</h2>
<ol><li>Java内存模型是什么？<br>
<ul><li><a href='http://www.jcp.org/en/jsr/detail?id=133'>http://www.jcp.org/en/jsr/detail?id=133</a>
</li></ul></li><li>synchronized的作用是什么？<br>
</li><li>volatile的作用是什么？<br>
</li><li>以下代码是不是线程安全？为什么？如果为count加上volatile修饰是否能够做到线程安全？你觉得该怎么做是线程安全的？<br>
<pre><code>public class Sample {<br>
  private static int count = 0;<br>
<br>
  public static void increment() {<br>
    count++;<br>
  }<br>
}<br>
</code></pre>
</li><li>学会使用 <code>java.uti.concurrent.**</code>
<ul><li><code>java.util.concurrent.locks.ReentrantLock</code>
</li><li><code>java.util.concurrent.locks.ReentrantReadWriteLock</code>
</li><li><code>java.util.concurrent.atomic.Atomic*</code>
</li><li><code>java.util.concurrent.ConcurrentHashMap</code>
</li><li><code>java.util.concurrent.Executors</code>
</li><li><code>...</code>
</li></ul></li><li>分析并解释一下下面两段代码的差别：<br>
<pre><code>// 代码1<br>
public class Sample {<br>
  private static int count = 0;<br>
<br>
  synchronized public static void increment() {<br>
    count++;<br>
  }<br>
}<br>
<br>
// 代码2<br>
public class Sample {<br>
  private static AtomicInteger count = new AtomicInteger(0);<br>
<br>
  public static void increment() {<br>
    count.getAndIncrement();<br>
  }<br>
}<br>
</code></pre></li></ol>

<h4>相关资料</h4>
<code>[1].</code> <a href='http://www.cs.umd.edu/~pugh/java/memoryModel/'>http://www.cs.umd.edu/~pugh/java/memoryModel/</a><br>
<code>[2].</code> <a href='http://gee.cs.oswego.edu/dl/jmm/cookbook.html'>http://gee.cs.oswego.edu/dl/jmm/cookbook.html</a><br>
<code>[3].</code> <a href='http://book.douban.com/subject/10484692/'>http://book.douban.com/subject/10484692/</a><br>
<code>[4].</code> <a href='http://www.intel.com/content/www/us/en/processors/architectures-software-developer-manuals.html'>http://www.intel.com/content/www/us/en/processors/architectures-software-developer-manuals.html</a><br>

<h2>三、<code>Java</code>底层知识</h2>
<ol><li>学习了解字节码、class文件格式<br>
<ul><li><a href='http://en.wikipedia.org/wiki/Java_class_file'>http://en.wikipedia.org/wiki/Java_class_file</a>
</li><li><a href='http://en.wikipedia.org/wiki/Java_bytecode'>http://en.wikipedia.org/wiki/Java_bytecode</a>
</li><li><a href='http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings'>http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings</a>
</li></ul></li><li>写一个程序要求实现javap的功能（手工完成，不借助ASM等工具）<br>
<pre><code>如Java源代码：<br>
  public static void main(String[] args) {<br>
    int i = 0;<br>
    i += 1;<br>
    i *= 1;<br>
    System.out.println(i);<br>
  }<br>
<br>
编译后读取class文件输出以下代码：<br>
public static void main(java.lang.String[]);<br>
  Code:<br>
   Stack=2, Locals=2, Args_size=1<br>
   0:	iconst_0<br>
   1:	istore_1<br>
   2:	iinc	1, 1<br>
   5:	iload_1<br>
   6:	iconst_1<br>
   7:	imul<br>
   8:	istore_1<br>
   9:	getstatic	#2; //Field java/lang/System.out:Ljava/io/PrintStream;<br>
   12:	iload_1<br>
   13:	invokevirtual	#3; //Method java/io/PrintStream.println:(I)V<br>
   16:	return<br>
  LineNumberTable: <br>
   line 4: 0<br>
   line 5: 2<br>
   line 6: 5<br>
   line 7: 9<br>
   line 8: 16<br>
</code></pre>
</li><li>使用CGLIB做一个AOP程序<br>
<ul><li><a href='http://cglib.sourceforge.net/'>http://cglib.sourceforge.net/</a>
</li></ul></li><li>使用ASM实现上述CGLIB实现的功能<br>
<ul><li><a href='http://asm.ow2.org/'>http://asm.ow2.org/</a></li></ul></li></ol>

<h4>相关资料</h4>
<code>[1].</code> <a href='http://book.douban.com/subject/1138768/'>http://book.douban.com/subject/1138768/</a><br>
<code>[2].</code> <a href='http://book.douban.com/subject/6522893/'>http://book.douban.com/subject/6522893/</a><br>
<code>[3].</code> <a href='http://en.wikipedia.org/wiki/Java_class_file'>http://en.wikipedia.org/wiki/Java_class_file</a><br>
<code>[4].</code> <a href='http://en.wikipedia.org/wiki/Java_bytecode'>http://en.wikipedia.org/wiki/Java_bytecode</a><br>
<code>[5].</code> <a href='http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings'>http://en.wikipedia.org/wiki/Java_bytecode_instruction_listings</a><br>
<code>[6].</code> <a href='http://asm.ow2.org/'>http://asm.ow2.org/</a><br>
<code>[7].</code> <a href='http://cglib.sourceforge.net/'>http://cglib.sourceforge.net/</a><br>

<h2>四、网络编程知识</h2>
<ol><li>用Java写一个简单的静态文件的HTTP服务器<br>
<ul><li>实现客户端缓存功能，支持返回304<br>
</li><li>实现可并发下载一个文件<br>
</li><li>使用线程池处理客户端请求<br>
</li><li>使用nio处理客户端请求<br>
</li><li>支持简单的rewrite规则<br>
</li><li><i><b>上述功能在实现的时候需要满足“开闭原则”</b></i>
</li></ul></li><li>了解 <code>nginx</code> 和 <code>apache</code> 服务器的特性并搭建一个对应的服务器<br>
<ul><li><a href='http://nginx.org/'>http://nginx.org/</a>
</li><li><a href='http://httpd.apache.org/'>http://httpd.apache.org/</a>
</li></ul></li><li>用Java实现FTP、SMTP协议<br>
</li><li>什么是CDN？如果实现？DNS起到什么作用？<br>
<ul><li>搭建一个DNS服务器<br>
</li><li>搭建一个 <code>Squid</code> 或 <code>Apache Traffic Server</code> 服务器<br>
<ul><li><a href='http://www.squid-cache.org/'>http://www.squid-cache.org/</a>
</li><li><a href='http://trafficserver.apache.org/'>http://trafficserver.apache.org/</a></li></ul></li></ul></li></ol>

<h4>相关资料</h4>
<code>[1].</code> <a href='http://www.ietf.org/rfc/rfc2616.txt'>http://www.ietf.org/rfc/rfc2616.txt</a><br>
<code>[2].</code> <a href='http://tools.ietf.org/rfc/rfc5321.txt'>http://tools.ietf.org/rfc/rfc5321.txt</a><br>
<code>[3].</code> <a href='http://en.wikipedia.org/wiki/Open/closed_principle'>http://en.wikipedia.org/wiki/Open/closed_principle</a><br>

<h2>五、网络安全知识</h2>
<ol><li>什么是DES、AES？<br>
</li><li>什么是RSA、DSA？<br>
</li><li>什么是MD5，SHA1？<br>
</li><li>什么是SSL、TLS，为什么HTTPS相对比较安全？<br>
</li><li>什么是中间人攻击、如果避免中间人攻击？<br>
</li><li>什么是DOS、DDOS、CC攻击？<br>
</li><li>什么是CSRF攻击？<br>
</li><li>什么是CSS攻击？<br>
</li><li>什么是SQL注入攻击？<br>
</li><li>什么是Hash碰撞拒绝服务攻击？<br>
</li><li>了解并学习下面几种增强安全的技术<br>
<ul><li><a href='http://www.openauthentication.org/'>http://www.openauthentication.org/</a>
<ul><li><code>HOTP</code>  <a href='http://www.ietf.org/rfc/rfc4226.txt'>http://www.ietf.org/rfc/rfc4226.txt</a>
</li><li><code>TOTP</code> <a href='http://tools.ietf.org/rfc/rfc6238.txt'>http://tools.ietf.org/rfc/rfc6238.txt</a>
</li><li><code>OCRA</code> <a href='http://tools.ietf.org/rfc/rfc6287.txt'>http://tools.ietf.org/rfc/rfc6287.txt</a>
</li></ul></li><li><a href='http://en.wikipedia.org/wiki/Salt_(cryptography'>http://en.wikipedia.org/wiki/Salt_(cryptography</a>)<br>
</li></ul></li><li>用openssl签一个证书部署到apache或nginx</li></ol>

<h4>相关资料</h4>
<code>[1].</code> <a href='http://en.wikipedia.org/wiki/Cryptographic_hash_function'>http://en.wikipedia.org/wiki/Cryptographic_hash_function</a><br>
<code>[2].</code> <a href='http://en.wikipedia.org/wiki/Block_cipher'>http://en.wikipedia.org/wiki/Block_cipher</a><br>
<code>[3].</code> <a href='http://en.wikipedia.org/wiki/Public-key_cryptography'>http://en.wikipedia.org/wiki/Public-key_cryptography</a><br>
<code>[4].</code> <a href='http://en.wikipedia.org/wiki/Transport_Layer_Security'>http://en.wikipedia.org/wiki/Transport_Layer_Security</a><br>
<code>[5].</code> <a href='http://www.openssl.org/'>http://www.openssl.org/</a><br>
<code>[6].</code> <a href='https://code.google.com/p/google-authenticator/'>https://code.google.com/p/google-authenticator/</a><br>

<h2>六、<code>JavaScript</code>知识</h2>
<ol><li>什么是prototype？<br>
<ul><li>修改代码，使程序输出“1 3 5”：<br>
<ul><li><a href='http://jsfiddle.net/Ts7Fk/'>http://jsfiddle.net/Ts7Fk/</a>
</li></ul></li></ul></li><li>什么是闭包？<br>
<ul><li>看一下这段代码，并解释一下为什么按Button1时没有alert出“This is button: 1”，如何修改：<br>
<ul><li><a href='http://jsfiddle.net/FDPj3/1/'>http://jsfiddle.net/FDPj3/1/</a>
</li></ul></li></ul></li><li>了解并学习一个JS框架<br>
<ul><li>jQuery<br>
</li><li>ExtJS<br>
</li><li>Dojo<br>
</li><li>AngularJS<br>
</li></ul></li><li>写一个Greasemonkey插件<br>
<ul><li><a href='http://en.wikipedia.org/wiki/Greasemonkey'>http://en.wikipedia.org/wiki/Greasemonkey</a>
</li></ul></li><li>用node.js写个程序<br>
<ul><li><a href='http://nodejs.org/'>http://nodejs.org/</a></li></ul></li></ol>

<h4>相关资料</h4>
<code>[1].</code> <a href='http://www.ecmascript.org/'>http://www.ecmascript.org/</a><br>
<code>[2].</code> <a href='http://jsfiddle.net/'>http://jsfiddle.net/</a><br>
<code>[3].</code> <a href='http://jsbin.com/'>http://jsbin.com/</a><br>
<code>[4].</code> <a href='http://runjs.cn/'>http://runjs.cn/</a><br>
<code>[5].</code> <a href='http://userscripts.org/'>http://userscripts.org/</a><br>

<h2>七、编译原理知识</h2>
<ol><li>用Java实现以下表达式解析并返回结果（语法和Oracle中的<code>select sysdate-1 from dual</code>类似）：<br>
<pre><code> sysdate<br>
 sysdate - 1<br>
 sysdate - 1/24<br>
 sysdate - 1/(12*2)<br>
</code></pre>
</li><li>实现对一个List通过DSL筛选<br>
<pre><code>  QList&lt;Map&lt;String, Object&gt;&gt; mapList = new QList&lt;Map&lt;String, Object&gt;&gt;;<br>
  mapList.add({"name": "hatter test"});<br>
  mapList.add({"id": -1,"name": "hatter test"});<br>
  mapList.add({"id": 0, "name": "hatter test"});<br>
  mapList.add({"id": 1, "name": "test test"});<br>
  mapList.add({"id": 2, "name": "hatter test"});<br>
  mapList.add({"id": 3, "name": "test hatter"});<br>
  mapList.query("id is not null and id &gt; 0 and name like '%hatter%'");<br>
<br>
要求返回列表中匹配的对象，即最后两个对象；<br>
</code></pre>
</li><li>用Java实现以下程序（语法和变量作用域处理都和JavaScript类似）：<br>
<pre><code>代码：<br>
var a = 1;<br>
var b = 2;<br>
var c = function() {<br>
  var a = 3;<br>
  println(a);<br>
  println(b);<br>
};<br>
c();<br>
println(a);<br>
println(b);<br>
<br>
输出：<br>
3<br>
2<br>
1<br>
2<br>
</code></pre></li></ol>

<h4>相关资料</h4>
<code>[1].</code> <a href='http://en.wikipedia.org/wiki/Abstract_syntax_tree'>http://en.wikipedia.org/wiki/Abstract_syntax_tree</a><br>
<code>[2].</code> <a href='https://javacc.java.net/'>https://javacc.java.net/</a><br>
<code>[3].</code> <a href='http://www.antlr.org/'>http://www.antlr.org/</a><br>

<h2>八、数据库知识</h2>
<ol><li>关系型数据库<br>
<ul><li>MySQL<br>
<ul><li>如何看执行计划？<br>
</li><li>如何搭建MySQL主备？<br>
</li><li>binlog是什么？<br>
</li></ul></li><li>Derby, H2, PostgreSQL （任选一个）<br>
</li><li>SQLite<br>
</li></ul></li><li>图数据库<br>
<ul><li>neo4j<br>
</li></ul></li><li>宽表数据库<br>
<ul><li>Cassandra<br>
</li><li>HBase</li></ul></li></ol>

<h4>相关资料</h4>
<code>[1].</code> <a href='http://db-engines.com/en/ranking'>http://db-engines.com/en/ranking</a><br>

<h2>九、其他知识</h2>
<ol><li>学习使用git<br>
<ul><li><a href='https://github.com/'>https://github.com/</a>
</li><li><a href='https://git.oschina.net/'>https://git.oschina.net/</a>
</li></ul></li><li>学习使用gradle<br>
<ul><li><a href='http://www.gradle.org/'>http://www.gradle.org/</a>
</li></ul></li><li>学习一个小语种语言<br>
<ul><li><code>Groovy</code>
</li><li><code>Scala</code>
</li><li><code>LISP, Common LISP, Schema, Clojure</code>
</li><li><code>R</code>
</li><li><code>Julia</code>
</li><li><code>Lua</code>
</li><li><code>Ruby</code>
</li><li><code>...</code>
</li></ul></li><li>尝试了解编码的本质<br>
<ul><li>了解以下概念<br>
<ul><li><code>ASCII, ISO-8859-1</code>
</li><li><code>GB2312, GBK, GB18030</code>
</li><li><code>Unicode, UTF-8</code>
</li></ul></li><li>不使用 <code>String.getBytes()</code> 等其他工具类/函数完成下面功能<br>
<pre><code>    public static void main(String[] args) throws IOException {<br>
        String str = "Hello, 我们是中国人。";<br>
        byte[] utf8Bytes = toUTF8Bytes(str);<br>
        FileOutputStream fos = new FileOutputStream("f.txt");<br>
        fos.write(utf8Bytes);<br>
        fos.close();<br>
    }<br>
<br>
    public static byte[] toUTF8Bytes(String str) {<br>
        return null; // TODO<br>
    }<br>
</code></pre>
</li><li>想一下上面的程序能不能写一个转GBK的？<br>
</li><li>写个程序自动判断一个文件是哪种编码<br>
</li></ul></li><li>尝试了解时间的本质<br>
<ul><li>了解以下概念<br>
<ul><li>时区 & 冬令时、夏令时<br>
<ul><li><a href='http://en.wikipedia.org/wiki/Time_zone'>http://en.wikipedia.org/wiki/Time_zone</a>
</li><li><a href='ftp://ftp.iana.org/tz/data/asia'>ftp://ftp.iana.org/tz/data/asia</a>
</li><li><a href='http://zh.wikipedia.org/wiki/%E4%B8%AD%E5%9C%8B%E6%99%82%E5%8D%80'>http://zh.wikipedia.org/wiki/%E4%B8%AD%E5%9C%8B%E6%99%82%E5%8D%80</a>
</li></ul></li><li>闰年<br>
<ul><li><a href='http://en.wikipedia.org/wiki/Leap_year'>http://en.wikipedia.org/wiki/Leap_year</a>
</li></ul></li><li>闰秒<br>
<ul><li><a href='ftp://ftp.iana.org/tz/data/leapseconds'>ftp://ftp.iana.org/tz/data/leapseconds</a>
</li></ul></li></ul></li><li><code>System.currentTimeMillis()</code> 返回的时间是什么</li></ul></li></ol>

<h4>相关资料</h4>
<code>[1].</code> <a href='http://git-scm.com/'>http://git-scm.com/</a><br>
<code>[2].</code> <a href='http://en.wikipedia.org/wiki/UTF-8'>http://en.wikipedia.org/wiki/UTF-8</a><br>
<code>[3].</code> <a href='http://www.iana.org/time-zones'>http://www.iana.org/time-zones</a><br>

<h2>十、综合应用</h2>
<ol><li>参考node.js用Java实现一个 :)</li></ol>

<h4>相关资料</h4>
<code>[1].</code> <i>以上大部分相关资料</i><br>

<br>
<hr />
<h2>【别人学什么】</h2>
<table><thead><th> <b>前端工程师学什么？</b> </th><th> <a href='https://github.com/JacksonTian/fks'>https://github.com/JacksonTian/fks</a> </th></thead><tbody>
<tr><td> <b><a href='http://weibo.com/dirlt'>@章炎-友盟</a></b> </td><td> <a href='http://dirlt.com/'>http://dirlt.com/</a> </td></tr>
<tr><td> <b>七天学会NodeJS</b> </td><td> <a href='http://nqdeng.github.io/7-days-nodejs/'>http://nqdeng.github.io/7-days-nodejs/</a> </td></tr>
<tr><td> <b>算法面试大全</b> </td><td> <a href='http://blog.csdn.net/v_july_v/article/details/6543438'>http://blog.csdn.net/v_july_v/article/details/6543438</a> </td></tr></tbody></table>

<h2>【推荐阅读】</h2>
<table><thead><th> 大型网站技术架构 </th><th> <a href='http://book.douban.com/subject/25723064/'>http://book.douban.com/subject/25723064/</a> </th></thead><tbody>
<tr><td> 高性能MySQL </td><td> <a href='http://book.douban.com/subject/23008813/'>http://book.douban.com/subject/23008813/</a> </td></tr>
<tr><td> 七周七语言 </td><td> <a href='http://book.douban.com/subject/10555435/'>http://book.douban.com/subject/10555435/</a> </td></tr>
<tr><td> JavaScript权威指南 </td><td> <a href='http://book.douban.com/subject/10549733/'>http://book.douban.com/subject/10549733/</a> </td></tr>
<tr><td> 深度探索C++对象模型 </td><td> <a href='http://book.douban.com/subject/1091086/'>http://book.douban.com/subject/1091086/</a> </td></tr>
<tr><td> 支撑处理器的技术 </td><td> <a href='http://book.douban.com/subject/20271450/'>http://book.douban.com/subject/20271450/</a> </td></tr>
<tr><td> 深入Java虚拟机(原书第2版) </td><td> <a href='http://book.douban.com/subject/1138768/'>http://book.douban.com/subject/1138768/</a> </td></tr>
<tr><td> 深入理解Java虚拟机 </td><td> <a href='http://book.douban.com/subject/6522893/'>http://book.douban.com/subject/6522893/</a> </td></tr>
<tr><td> 代码大全（第2版） </td><td> <a href='http://book.douban.com/subject/1477390/'>http://book.douban.com/subject/1477390/</a> </td></tr>
<tr><td> 重构 </td><td> <a href='http://book.douban.com/subject/1229923/'>http://book.douban.com/subject/1229923/</a> </td></tr>
<tr><td> Effective Java 中文版 </td><td> <a href='http://book.douban.com/subject/1103015/'>http://book.douban.com/subject/1103015/</a> </td></tr>